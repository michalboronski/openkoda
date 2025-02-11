/*
MIT License

Copyright (c) 2016-2024, Openkoda CDX Sp. z o.o. Sp. K. <openkoda.com>

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software without restriction, including without limitation
the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice
shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR
A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.openkoda.service.dynamicentity;

import aj.org.objectweb.asm.Opcodes;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.openkoda.core.flow.LoggingComponent;
import com.openkoda.core.form.FieldDbType;
import com.openkoda.core.form.FieldType;
import com.openkoda.core.form.FrontendMappingDefinition;
import com.openkoda.core.form.FrontendMappingFieldDefinition;
import com.openkoda.core.service.form.FormService;
import com.openkoda.dto.CanonicalObject;
import com.openkoda.model.User;
import com.openkoda.model.common.OpenkodaEntity;
import com.openkoda.model.common.SearchableRepositoryMetadata;
import com.openkoda.model.component.Form;
import com.openkoda.model.file.EntityWithFiles;
import com.openkoda.model.file.File;
import com.openkoda.repository.SearchableRepositories;
import com.openkoda.repository.SecureRepository;
import jakarta.persistence.*;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.modifier.ModifierContributor;
import net.bytebuddy.description.modifier.TypeManifestation;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.MethodCall;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Formula;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple4;
import reactor.util.function.Tuples;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static com.openkoda.core.helper.NameHelper.toColumnName;
import static com.openkoda.model.common.ModelConstants.REQUIRED_READ_PRIVILEGE;
import static com.openkoda.model.common.ModelConstants.REQUIRED_WRITE_PRIVILEGE;
import static java.lang.Character.isUpperCase;
import static java.util.stream.Collectors.toMap;
import static net.bytebuddy.description.modifier.Visibility.PROTECTED;
import static net.bytebuddy.description.modifier.Visibility.PUBLIC;
import static net.bytebuddy.dynamic.loading.ClassLoadingStrategy.Default.INJECTION;
import static net.bytebuddy.implementation.attribute.AnnotationValueFilter.Default.SKIP_DEFAULTS;

@Service
public class DynamicEntityRegistrationService implements LoggingComponent {

    public static final String PACKAGE = "com.openkoda.dynamicentity.generated.";
    public static Map<String, Tuple2<String, Class<? extends SecureRepository<? extends OpenkodaEntity>>>> dynamicRepositoryClasses = new HashMap<>();
    public static Map<String, Class<? extends OpenkodaEntity>> dynamicEntityClasses = new HashMap<>();

    @PersistenceContext
    EntityManager em;
    
    public void registerDynamicRepositories(boolean proceed){
        if(!proceed){
            return;
        }
        for(Map.Entry<String, Tuple2<String, Class<? extends SecureRepository<? extends OpenkodaEntity>>>> entry : dynamicRepositoryClasses.entrySet()) {
            String tableName = entry.getKey();
            Class<? extends SecureRepository<? extends OpenkodaEntity>> repositoryClass = entry.getValue().getT2();

            SearchableRepositories.registerSearchableRepository(tableName, new JpaRepositoryFactory(em).getRepository(repositoryClass));
        }
    }

    /**
     * generate dynamicEntityDescriptors for forms
     */
    public static int generateDynamicEntityDescriptors(List<Form> forms, Long timeMillis) {
        debugLogger.debug("[generateDynamicEntityDescriptors] forms to process: {}", forms.size());
        return generateDynamicEntityDescriptors(forms, forms.stream().collect(toMap(Form::getName, FormService::getFrontendMappingDefinition)), timeMillis);
    }

    public static int generateDynamicEntityDescriptors(List<Form> forms, Map<String, FrontendMappingDefinition> frontendMappingDefinitions, Long timeMillis) {
        debugLogger.debug("[generateDynamicEntityDescriptors] forms to process: {}", forms.size());
        int generatedEntities = 0;

        for(Form form : forms) {
            String formName = form.getName();
            String tableName = form.getTableName();
            List<FrontendMappingFieldDefinition> fields = Arrays.asList(frontendMappingDefinitions.get(form.getName()).getDbTypeFieldsImplicitlyDefined());
            DynamicEntityDescriptorFactory.create(formName, tableName, fields, timeMillis);
            generatedEntities++;
        }


        return generatedEntities;
    }

    public static void buildAndLoadDynamicClasses(ClassLoader classLoader) throws IOException, URISyntaxException {
        debugLogger.debug("[buildAndLoadDynamicClasses]");
        Map<String, Tuple4<DynamicType.Unloaded<OpenkodaEntity>, String, String, List<String>>> unloadedClasses = new HashMap<>();

//        create unloaded types
        for (DynamicEntityDescriptor descriptor : DynamicEntityDescriptorFactory.loadableInstances()) {
            Tuple4<DynamicType.Unloaded<OpenkodaEntity>, String, String, List<String>> dynamicEntity = createDynamicEntityType(descriptor);
            unloadedClasses.put(descriptor.getEntityKey(), dynamicEntity);
        }

//        load all dynamic types
        for (DynamicEntityDescriptor descriptor : DynamicEntityDescriptorFactory.loadableInstances()) {
            Tuple4<DynamicType.Unloaded<OpenkodaEntity>, String, String, List<String>> dynamicClass = unloadedClasses.get(descriptor.getEntityKey());
            DynamicType.Unloaded<OpenkodaEntity> t1 = dynamicClass.getT1();
            for(String type : dynamicClass.getT4()) {
                Tuple4<DynamicType.Unloaded<OpenkodaEntity>, String, String, List<String>> includeType = unloadedClasses.get(type);
                if(includeType != null) {
                    t1.include(includeType.getT1());
                }
            }
            dynamicEntityClasses.put(descriptor.getEntityKey(), t1.load(classLoader, INJECTION).getLoaded());
            debugLogger.debug("[buildAndLoadDynamicClasses] loaded entity class for key: {}", descriptor.getEntityKey());
        }

//        create and load repositories for all dynamic types
        for (DynamicEntityDescriptor descriptor : DynamicEntityDescriptorFactory.loadableInstances()) {
            Tuple4<DynamicType.Unloaded<OpenkodaEntity>, String, String, List<String>> dynamicEntityTuple = unloadedClasses.get(descriptor.getEntityKey());
            Class<? extends SecureRepository<? extends OpenkodaEntity>> dynamicRepository = createAndLoadDynamicRepository(
                    dynamicEntityClasses.get(descriptor.getEntityKey()), //generated entity class
                    descriptor.getEntityKey(),
                    dynamicEntityTuple.getT2(), //description formula
                    dynamicEntityTuple.getT3(), //search index formula
                    descriptor.getSuffixedRepositoryName(),
                    classLoader);
            dynamicRepositoryClasses.put(descriptor.getTableName(), Tuples.of(descriptor.getSuffixedEntityClassName().toLowerCase(), dynamicRepository));
            descriptor.setLoaded(true);
            debugLogger.debug("[buildAndLoadDynamicClasses] loaded repository class for key: {}", descriptor.getEntityKey());
        }
    }

    private static <O extends OpenkodaEntity> Tuple4<DynamicType.Unloaded<OpenkodaEntity>, String, String, List<String>> createDynamicEntityType(
            DynamicEntityDescriptor dynamicEntityDescriptor) {
        String name = dynamicEntityDescriptor.getSuffixedEntityClassName();
        String tableName = dynamicEntityDescriptor.getTableName();
        Collection<FrontendMappingFieldDefinition> fields = dynamicEntityDescriptor.getFields();
        String entityKey = dynamicEntityDescriptor.getEntityKey();
        debugLogger.debug("[createDynamicEntityType] {} {}", name, tableName);

        TypeDescription userTypeDescription = TypeDescription.ForLoadedType.of(User.class);
        TypeDescription entityWithFilesType = TypeDescription.ForLoadedType.of(EntityWithFiles.class);
        AnnotationDescription entity = AnnotationDescription.Builder.ofType(Entity.class)
                .build();
        AnnotationDescription.Builder formulaType = AnnotationDescription.Builder.ofType(Formula.class);
        AnnotationDescription formula = formulaType
                .define("value", "(null)")
                .build();
        AnnotationDescription tableAnnotation = AnnotationDescription.Builder.ofType(Table.class)
                .define("name", tableName)
                .build();
        AnnotationDescription.Builder columnAnnotation = AnnotationDescription.Builder.ofType(Column.class)
                .define("updatable", true)
                .define("insertable", true);
//        files annotations
        AnnotationDescription foreignKeyAnnotation = AnnotationDescription.Builder.ofType(ForeignKey.class)
                .define("value", ConstraintMode.NO_CONSTRAINT).build();
        AnnotationDescription.Builder joinColumnAnnotation = AnnotationDescription.Builder.ofType(JoinColumn.class);
        AnnotationDescription.Builder joinColumnNotUpdatable = joinColumnAnnotation.define("insertable", false)
                .define("updatable", false);

        TypeDescription joinColumnAnnotationType = AnnotationDescription.Builder.ofType(JoinColumn.class).build().getAnnotationType();
        AnnotationDescription jsonIgnoreAnnotation = AnnotationDescription.Builder.ofType(JsonIgnore.class).build();
        AnnotationDescription manyToManyAnnotation = AnnotationDescription.Builder.ofType(ManyToMany.class)
                .define("fetch", FetchType.LAZY).build();
        AnnotationDescription joinTableAnnotation = AnnotationDescription.Builder.ofType(JoinTable.class)
                .define("name", "file_reference")
                .define("foreignKey", foreignKeyAnnotation)
                .defineAnnotationArray("inverseJoinColumns", joinColumnAnnotationType, joinColumnAnnotation
                        .define("name", "file_id")
                        .build())
                .defineAnnotationArray("joinColumns", joinColumnAnnotationType, joinColumnAnnotation
                        .define("name", "organization_related_entity_id")
                        .define("insertable", false)
                        .define("updatable", false)
                        .build())
                .build();

        AnnotationDescription elementCollectionAnnotation = AnnotationDescription.Builder.ofType(ElementCollection.class)
                .define("fetch", FetchType.LAZY)
                .define("targetClass", Long.class).build();
        AnnotationDescription collectionTableAnnotation = AnnotationDescription.Builder.ofType(CollectionTable.class)
                .define("name", "file_reference")
                .defineAnnotationArray("joinColumns", joinColumnAnnotationType, joinColumnAnnotation
                        .define("name", "organization_related_entity_id")
                        .build())
                .define("foreignKey", foreignKeyAnnotation).build();
        AnnotationDescription orderColumnAnnotation = AnnotationDescription.Builder.ofType(OrderColumn.class)
                .define("name", "sequence").build();
        AnnotationDescription manyToOneAnnotation = AnnotationDescription.Builder.ofType(ManyToOne.class).build();

        DynamicType.Unloaded<OpenkodaEntity> entityType = null;
        StringBuilder descriptionFormula = new StringBuilder();
        StringBuilder searchIndexFormula = new StringBuilder();
        List<String> includeTypes = new ArrayList<>();

        try {
            DynamicType.Builder<OpenkodaEntity>  dynamicType;

                dynamicType = new ByteBuddy()
                        .with(SKIP_DEFAULTS)
                        .subclass(OpenkodaEntity.class)
                        .implement(CanonicalObjectInterceptor.class)
                        .modifiers(ModifierContributor.Resolver.of(Visibility.PUBLIC, TypeManifestation.FINAL).resolve())
                        .name(PACKAGE + name)
                        .annotateType(entity)
                        .annotateType(tableAnnotation)
                        .defineConstructor(PUBLIC)
                        .intercept(MethodCall
                                .invoke(OpenkodaEntity.class.getDeclaredConstructor(Long.class))
                                .with((Object) null));

            for(FrontendMappingFieldDefinition field : fields) {
                Type fieldJavaType = getFieldJavaType(field);
                String dbColumnName = toColumnName(field.getValueName());

                FieldType type = field.getType();
                if (type.equals(FieldType.files)) {
                    String filesFieldName = "files";
                    String filesIdFieldName = "filesId";
                    //[adrysch] if there's need for multiple files fields in a entity, this is the place to start
//                    String filesFieldName = field.getName();
//                    String filesIdFieldName = field.getValueName();
                    dynamicType = dynamicType.implement(entityWithFilesType);
                    dynamicType = dynamicType.defineField(filesFieldName, listOfType(File.class), PUBLIC)
                            .annotateField(manyToManyAnnotation)
                            .annotateField(joinTableAnnotation)
                            .annotateField(jsonIgnoreAnnotation)
                            .annotateField(orderColumnAnnotation)
                            .defineMethod(getGetterName(filesFieldName) , listOfType(File.class), PUBLIC).intercept(FieldAccessor.ofField(filesFieldName))
                            .defineMethod(getSetterName(filesFieldName), void.class, PUBLIC).withParameter(listOfType(File.class)).intercept(FieldAccessor.ofField(filesFieldName));
                    dynamicType = dynamicType.defineField(filesIdFieldName, listOfType(Long.class), PUBLIC)
                            .annotateField(columnAnnotation.define("name", "file_id").build())
                            .annotateField(elementCollectionAnnotation)
                            .annotateField(collectionTableAnnotation)
                            .annotateField(orderColumnAnnotation)
                            .defineMethod(getGetterName(filesIdFieldName), listOfType(Long.class), PUBLIC).intercept(FieldAccessor.ofField(filesIdFieldName))
                            .defineMethod(getSetterName(filesIdFieldName), void.class, PUBLIC).withParameter(listOfType(Long.class)).intercept(FieldAccessor.ofField(filesIdFieldName));
                } else {
                    FieldDbType dbType = type.getDbType();
                    if (dbType != null && dbType.equals(FieldType.text.getDbType())) {
                        descriptionFormula.append("||' '||").append(String.format("COALESCE(%s,'')", dbColumnName));
                    }
                    if (dbType != null && dbType.getColumnType().equals(FieldType.text.getDbType().getColumnType())) {
                        searchIndexFormula.append("||' '||").append(String.format("'%s:'||COALESCE(%s,'')", field.getName(), dbColumnName));
                    } else if (type.equals(FieldType.many_to_one)) {
                        searchIndexFormula.append("||' '||").append(String.format("'%s:'||COALESCE(cast (%s as varchar),'')", field.getName(), dbColumnName));

                        if ("user".equals(field.referencedEntityKey)) {
                            dynamicType = dynamicType.defineField(field.getName(), userTypeDescription, Opcodes.ACC_PUBLIC)
                                    .annotateField(manyToOneAnnotation)
                                    .annotateField(joinColumnNotUpdatable.define("name", toColumnName(field.getValueName())).build())
                                    .defineMethod(getGetterName(field.getName()), userTypeDescription, PUBLIC).intercept(FieldAccessor.ofField(field.getName()))
                                    .defineMethod(getSetterName(field.getName()), void.class, PUBLIC).withParameter(userTypeDescription).intercept(FieldAccessor.ofField(field.getName()));

                        } else {
                            DynamicEntityDescriptor instanceByEntityKey = DynamicEntityDescriptorFactory.getInstanceByEntityKey(field.referencedEntityKey);
                            if (instanceByEntityKey != null) {
                                TypeDescription.Latent referenceTypeDescription = instanceByEntityKey.getTypeDescription();
                                includeTypes.add(field.referencedEntityKey);
                                dynamicType = dynamicType.defineField(field.getName(), referenceTypeDescription, Opcodes.ACC_PUBLIC)
                                        .annotateField(manyToOneAnnotation)
                                        .annotateField(joinColumnNotUpdatable.define("name", toColumnName(field.getValueName())).build())
                                        .defineMethod(getGetterName(field.getName()), referenceTypeDescription, PUBLIC).intercept(FieldAccessor.ofField(field.getName()))
                                        .defineMethod(getSetterName(field.getName()), void.class, PUBLIC).withParameter(referenceTypeDescription).intercept(FieldAccessor.ofField(field.getName()));
                            }
                        }
                    } else if (type.equals(FieldType.one_to_many)) {
                        DynamicEntityDescriptor instanceByEntityKey = DynamicEntityDescriptorFactory.getInstanceByEntityKey(field.referencedEntityKey);
                        if (instanceByEntityKey != null) {
                            TypeDescription.Latent referenceTypeDescription = instanceByEntityKey.getTypeDescription();

                            AnnotationDescription oneToManyAnnotation = AnnotationDescription.Builder.ofType(OneToMany.class)
                                    .define("mappedBy", field.mappedByFieldName)
                                    .define("fetch", FetchType.LAZY)
                                    .defineEnumerationArray("cascade", CascadeType.class, CascadeType.ALL)
                                    .define("orphanRemoval", true)
                                    .define("targetEntity", referenceTypeDescription)
                                    .build();

                            String referenceFieldName = StringUtils.substringBeforeLast(toColumnName(field.getName()), "_");
                            TypeDescription.Generic collection = TypeDescription.Generic.Builder.parameterizedType(
                                    TypeDescription.ForLoadedType.of(List.class),
                                    referenceTypeDescription
                            ).build();

                            dynamicType = dynamicType.defineField(referenceFieldName, collection, Opcodes.ACC_PUBLIC)
                                    .annotateField(oneToManyAnnotation)
                                    .defineMethod(getGetterName(referenceFieldName), collection, PUBLIC).intercept(FieldAccessor.ofField(referenceFieldName))
                                    .defineMethod(getSetterName(referenceFieldName), void.class, PUBLIC).withParameter(collection).intercept(FieldAccessor.ofField(referenceFieldName))
                            ;
                        }
                        continue; // no more actions needed fo one-to-many relationship, this if branch has everything that is needed
                    }
                    AnnotationDescription fieldDbAnnotation =
                    StringUtils.isNotBlank(field.sqlFormula) ?
                        formulaType.define("value", String.format("(%s)", field.sqlFormula)).build():
                        columnAnnotation.define("name", dbColumnName).build();

                    dynamicType = dynamicType.defineField(field.getValueName(), fieldJavaType, PUBLIC)
                                .annotateField(fieldDbAnnotation)
                                .defineMethod(getGetterName(field.getValueName()), fieldJavaType, PUBLIC).intercept(FieldAccessor.ofField(field.getValueName()))
                                .defineMethod(getSetterName(field.getValueName()), void.class, PUBLIC).withParameter(fieldJavaType).intercept(FieldAccessor.ofField(field.getValueName()));
                }
            }
            entityType = dynamicType
                     .defineField(REQUIRED_READ_PRIVILEGE, String.class, PROTECTED)
                     .annotateField(formula)
                     .defineField(REQUIRED_WRITE_PRIVILEGE, String.class, PROTECTED)
                     .annotateField(formula)
                     .defineMethod("getRequiredReadPrivilege", String.class, PUBLIC)
                     .intercept(FieldAccessor.ofBeanProperty())
                     .defineMethod("getRequiredWritePrivilege", String.class, PUBLIC)
                     .intercept(FieldAccessor.ofBeanProperty())
                     .make();
        } catch (NoSuchMethodException e) {
            debugLogger.error("[createDynamicEntityType]", e);
        }

        return Tuples.of(entityType, String.format("(''||id%s)", descriptionFormula), String.format("('%s:'||id %s)", entityKey, searchIndexFormula),includeTypes);
    }

    private static <T extends OpenkodaEntity> Class<SecureRepository<T>> createAndLoadDynamicRepository(Class<T> entity,
                                                                                                        String entityKey,
                                                                                                        String entityDescriptionFormula,
                                                                                                        String searchIndexFormula,
                                                                                                        String repositoryName,
                                                                                                        ClassLoader cl) {
        debugLogger.debug("[createAndLoadDynamicRepository] {}", entityKey);

        AnnotationDescription repositoryAnnotation = AnnotationDescription.Builder.ofType(Repository.class)
                .build();
        AnnotationDescription searchableRepositoryAnnotation = AnnotationDescription.Builder.ofType(SearchableRepositoryMetadata.class)
                .define("entityClass", entity)
                .define("entityKey", entityKey)
                .define("searchIndexFormula", searchIndexFormula)
                .define("descriptionFormula", entityDescriptionFormula)
                .build();

        TypeDescription.Generic secureRepositoryType = TypeDescription.Generic.Builder
                .parameterizedType(SecureRepository.class, entity)
                .build();

        DynamicType.Unloaded<?> repositoryType = new ByteBuddy()
                .with(SKIP_DEFAULTS)
                .makeInterface()
                .implement(secureRepositoryType)
                .annotateType(repositoryAnnotation, searchableRepositoryAnnotation)
                .name(PACKAGE + repositoryName)
                .make();

        Class<?> repo = repositoryType
                .load(cl, INJECTION)
                .getLoaded();

        return (Class<SecureRepository<T>>) repo;
    }
    
    public interface CanonicalObjectInterceptor extends CanonicalObject {

        default String notificationMessage() {
            StringBuilder builder = new StringBuilder();
            //builder.append(this.getClass().getSimpleName());
            builder.append("{");
            try {
                List<String> fields = java.util.stream.Stream.of(this.getClass().getDeclaredFields())
                        .filter( f -> !java.lang.reflect.Modifier.isFinal(f.getModifiers()) && !java.lang.reflect.Modifier.isStatic(f.getModifiers()) && java.lang.reflect.Modifier.isPublic(f.getModifiers()))
                .map( f -> {
                    try {
                        return String.format("\"%s\":\"%s\"", f.getName(),f.get(this) != null ? f.get(this).toString() : "null");
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        debugLogger.warn("[notificationMessage] could not create default notification message, returning empty string, reason: {}", e.getMessage());
                        return "";
                    }
                }).toList();
                
                try {
                    Method idField = this.getClass().getMethod("getId", null);
                    if(idField != null) {
                        builder.append("\"id\" : ");
                        builder.append(idField.invoke(this) != null ? idField.invoke(this).toString() : "null");
                        builder.append(", ");
                    }
                } catch(NoSuchMethodException exc) {
                    exc.printStackTrace();
                }
                
                builder.append(String.join(", ", fields));
                builder.append("}");
    
                return builder.toString();
            }catch (Throwable exc) {
                exc.printStackTrace();
                return this.toString();
            }
        }   
    }

    private static TypeDescription.Generic listOfType(Class c){
        return TypeDescription.Generic.Builder.parameterizedType(List.class, c).build();
    }

    private static Type getFieldJavaType(FrontendMappingFieldDefinition field) {
        if (field.type == FieldType.one_to_many) {
            return List.class;
        }
        if (field.type.getDbType().getColumnType().equals("varchar")) {
            return String.class;
        }
        return switch (field.type.getDbType()) {
            case BIGINT -> Long.class;
            case NUMERIC -> BigDecimal.class;
            case BOOLEAN -> Boolean.class;
            case DATE -> LocalDate.class;
            case TIMESTAMP_W_TZ -> LocalDateTime.class;
            case TIME_W_TZ -> LocalTime.class;
            default -> null;
        };
    }

    private static String getGetterName(String fieldName){
        return "get" + capitalize(fieldName);
    }

    private static String getSetterName(String fieldName){
        return "set" + capitalize(fieldName);
    }

    private static String capitalize(String fieldName){
        if(fieldName.length() > 1 && isUpperCase(fieldName.charAt(1))){
            return fieldName;
        }
        return StringUtils.capitalize(fieldName);

    }
}