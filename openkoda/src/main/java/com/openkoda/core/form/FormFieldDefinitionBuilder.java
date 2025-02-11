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

package com.openkoda.core.form;

import com.openkoda.core.helper.NameHelper;
import com.openkoda.core.helper.PrivilegeHelper;
import com.openkoda.core.security.OrganizationUser;
import com.openkoda.model.PrivilegeBase;
import com.openkoda.model.common.LongIdEntity;
import com.openkoda.repository.SecureEntityDictionaryRepository;
import com.openkoda.uicomponent.annotation.Autocomplete;
import io.micrometer.common.util.StringUtils;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.openkoda.core.helper.PrivilegeHelper.valueOfString;

public class FormFieldDefinitionBuilder<V> extends FormFieldDefinitionBuilderStart {


    @Autocomplete
    public FrontendMappingFieldDefinition[] getFieldsAsArray() {
        return fields.toArray(new FrontendMappingFieldDefinition[fields.size()]);
    }
    @Autocomplete
    public Tuple2<FrontendMappingFieldDefinition, Function<?, String>>[] getFieldValidatorsAsArray() {
        return fieldValidators.toArray(new Tuple2[fieldValidators.size()]);
    }
    @Autocomplete
    public Function<? extends Form, Map<String, String>>[]  getFormValidatorsAsArray() {
        return formValidators.toArray(new Function[formValidators.size()]);
    }
    @Autocomplete
    public FormFieldDefinitionBuilder(String formName, PrivilegeBase defaultReadPrivilege, PrivilegeBase defaultWritePrivilege) {
        super(formName, defaultReadPrivilege, defaultWritePrivilege);
    }
    @Autocomplete
    public FormFieldDefinitionBuilder<V> additionalCss(String additionalCss) {
        lastField = new FrontendMappingFieldDefinitionBuilder()
                .withMapping(lastField)
                .withAdditionalCss(additionalCss)
                .createFrontendMappingFieldDefinition();
        fields.set(fields.size() - 1, lastField);
        return this;
    }

    @Autocomplete
    public FormFieldDefinitionBuilder<V> searchable() {
        fields.set(fields.size() - 1, lastField = new FrontendMappingFieldDefinitionBuilder()
                .withMapping(lastField)
                .withSearchEnabled(true)
                .createFrontendMappingFieldDefinition());
        return this;
    }

    @Autocomplete
    public FormFieldDefinitionBuilder<V> sqlFormula(String sqlFormula) {
        fields.set(fields.size() - 1, lastField = new FrontendMappingFieldDefinitionBuilder()
                .withMapping(lastField)
                .withSqlFormula(sqlFormula)
                .createFrontendMappingFieldDefinition());

        return this;
    }

    public FormFieldDefinitionBuilder<V> additionalPrivileges(PrivilegeBase readPrivilege, PrivilegeBase writePrivilege) {
        fields.set(fields.size() - 1, lastField = new FrontendMappingFieldDefinitionBuilder()
                .withMapping(lastField)
                .withReadPrivilege(readPrivilege)
                .withWritePrivilege(writePrivilege)
                .createFrontendMappingFieldDefinition());
        return this;
    }

    @Autocomplete
    public FormFieldDefinitionBuilder<V> additionalPrivileges(String readPrivilege, String writePrivilege) {
        fields.set(fields.size() - 1, lastField = new FrontendMappingFieldDefinitionBuilder()
                .withMapping(lastField)
                .withReadPrivilege(valueOfString(readPrivilege))
                .withWritePrivilege(valueOfString(writePrivilege))
                .createFrontendMappingFieldDefinition());
        return this;
    }

    @Autocomplete
    public FormFieldDefinitionBuilder<V> additionalPrivileges(BiFunction<OrganizationUser, LongIdEntity, Boolean> canReadCheck, BiFunction<OrganizationUser, LongIdEntity, Boolean> canWriteCheck) {
        fields.set(fields.size() - 1, lastField = new FrontendMappingFieldDefinitionBuilder()
                .withMapping(lastField)
                .withCanReadCheck(canReadCheck)
                .withCanWriteCheck(canWriteCheck)
                .createFrontendMappingFieldDefinition());
        return this;
    }

    @Autocomplete
    public FormFieldDefinitionBuilder<V> visible(BiFunction<OrganizationUser, LongIdEntity, Boolean> canReadCheck) {
        fields.set(fields.size() - 1, lastField = new FrontendMappingFieldDefinitionBuilder()
                .withMapping(lastField)
                .withCanReadCheck(canReadCheck)
                .withStrictReadAccess(true)
                .createFrontendMappingFieldDefinition()
        );
        return this;
    }

    @Autocomplete
    public FormFieldDefinitionBuilder<V> enabled(BiFunction<OrganizationUser, LongIdEntity, Boolean> canWriteCheck) {
        fields.set(fields.size() - 1, lastField = new FrontendMappingFieldDefinitionBuilder()
                .withMapping(lastField)
                .withCanWriteCheck(canWriteCheck)
                .withStrictWriteAccess(true)
                .createFrontendMappingFieldDefinition()
        );
        return this;
    }
    
    public FormFieldDefinitionBuilder<V> additionalAction(String actionLabelKey, String actionUrl, PrivilegeBase additionalActionPrivilege) {
        fields.set(fields.size() - 1, lastField = new FrontendMappingFieldDefinitionBuilder()
                .withMapping(lastField)
                .withActionLabelKey(actionLabelKey)
                .withActionUrl(actionUrl)
                .withActionPrivilege(additionalActionPrivilege)
                .createFrontendMappingFieldDefinition()
        );
        return this;
    }

    @Autocomplete
    public FormFieldDefinitionBuilder<V> additionalAction(String actionLabelKey, String actionUrl, String privilegeNameAsString) {
        fields.set(fields.size() - 1, lastField = new FrontendMappingFieldDefinitionBuilder()
                .withMapping(lastField)
                .withActionLabelKey(actionLabelKey)
                .withActionUrl(actionUrl)
                .withActionPrivilege(PrivilegeHelper.valueOfString(privilegeNameAsString))
                .createFrontendMappingFieldDefinition()
        );
        return this;
    }

    @Autocomplete
    public FormFieldDefinitionBuilder<V> valueSupplier(Function<AbstractForm, Object> valueSupplier) {
        fields.set(fields.size() - 1, lastField = new FrontendMappingFieldDefinitionBuilder()
                .withMapping(lastField)
                .withValueSupplier(valueSupplier)
                .createFrontendMappingFieldDefinition()
        );
        return this;
    }

    public <V2> FormFieldDefinitionBuilder<V2> valueConverters(Function<V, V2> toEntityValue, Function<Object, Object> toDtoValue) {
        fields.set(fields.size() - 1, lastField = new FrontendMappingFieldDefinitionBuilder()
                .withMapping(lastField)
                .withDtoToEntityValueConverter(toEntityValue)
                .withEntityToDtoValueConverter(toDtoValue)
                .createFrontendMappingFieldDefinition()
        );
        return (FormFieldDefinitionBuilder<V2>) this;
    }

    @Autocomplete
    public FormFieldDefinitionBuilder<V> fragment(String fragmentName) {
        fields.set(fields.size() - 1, lastField = new FrontendMappingFieldDefinitionBuilder()
                .withMapping(lastField)
                .withFragmentName(fragmentName)
                .createFrontendMappingFieldDefinition()
        );
        return this;
    }

    @Deprecated
    public FormFieldDefinitionBuilder<V> referenceDescriptionSource(String source) {
        FrontendMappingFieldDefinition datalistField = fields.get(fields.size() - 2);
        FrontendMappingFieldDefinition dropdownField = fields.get(fields.size() - 1);
        fields.set(fields.size() - 2, new FrontendMappingFieldDefinitionBuilder()
                .withMapping(datalistField)
                .withDatalistSupplier((f, d) -> d.dictionary(dropdownField.referencedEntityKey, source))
                .createFrontendMappingFieldDefinition()
        );
        return this;
    }

    @Autocomplete
    public FormFieldDefinitionBuilder<V> descriptionSource(String source) {
        FrontendMappingFieldDefinition datalistField = fields.get(fields.size() - 2);
        FrontendMappingFieldDefinition dropdownField = fields.get(fields.size() - 1);
        fields.set(fields.size() - 1, new FrontendMappingFieldDefinitionBuilder()
                .withMapping(dropdownField)
                .withDescriptionSource(source)
                .createFrontendMappingFieldDefinition()
        );
        fields.set(fields.size() - 2, new FrontendMappingFieldDefinitionBuilder()
                .withMapping(datalistField)
                .withDatalistSupplier((f, d) -> d.dictionary(dropdownField.referencedEntityKey, NameHelper.toColumnName(source)))
                .createFrontendMappingFieldDefinition()
        );
        return this;
    }

    @Autocomplete
    public FormFieldDefinitionBuilder<V> validate(Function<V, String> validatorReturningErrorCode) {
        fieldValidators.add(Tuples.of(lastField, validatorReturningErrorCode));
        return this;
    }

    @Deprecated
    public FormFieldDefinitionBuilder<V> withPreselectedValue(String preselectedValue) {
        return preselectedValue(preselectedValue);
    }

    @Autocomplete
    public FormFieldDefinitionBuilder<V> preselectedValue(String preselectedValue) {
        fields.set(fields.size() - 1, lastField = new FrontendMappingFieldDefinitionBuilder()
                .withMapping(lastField)
                .withPreselectedValue(preselectedValue)
                .createFrontendMappingFieldDefinition());

        return this;
    }

    @Autocomplete
    public FormFieldDefinitionBuilder<V> validateForm(Function<? extends Form, Map<String, String>> validatorReturningRejectedFieldToErrorCodeMap) {
        formValidators.add(validatorReturningRejectedFieldToErrorCodeMap);
        return this;
    }

    @Autocomplete
    public FormFieldDefinitionBuilder<V> entityDescription(Function<? extends Form, Map<String, String>> validatorReturningRejectedFieldToErrorCodeMap) {
        formValidators.add(validatorReturningRejectedFieldToErrorCodeMap);
        return this;
    }

    @Autocomplete
    public FormFieldDefinitionBuilder<V> html() {
        fields.set(fields.size() - 1, lastField = new FrontendMappingFieldDefinitionBuilder()
                .withMapping(lastField)
                .withType(FieldType.code_html)
                .createFrontendMappingFieldDefinition()
        );
        return this;
    }

    @Autocomplete
    public FormFieldDefinitionBuilder<V> css() {
        fields.set(fields.size() - 1, lastField = new FrontendMappingFieldDefinitionBuilder()
                .withMapping(lastField)
                .withType(FieldType.code_css)
                .createFrontendMappingFieldDefinition()
        );
        return this;
    }

    @Autocomplete
    public FormFieldDefinitionBuilder<V> js() {
        fields.set(fields.size() - 1, lastField = new FrontendMappingFieldDefinitionBuilder()
                .withMapping(lastField)
                .withType(FieldType.code_js)
                .createFrontendMappingFieldDefinition()
        );
        return this;
    }

    @Autocomplete
    public FormFieldDefinitionBuilder<V> autocomplete(String autocompleteName) {
        fields.set(fields.size() - 1, lastField = new FrontendMappingFieldDefinitionBuilder()
                .withMapping(lastField)
                .withAutocomplete(autocompleteName)
                .createFrontendMappingFieldDefinition()
        );
        return this;
    }

    @Autocomplete
    public FormFieldDefinitionBuilder<V> required() {
        fields.set(fields.size() - 1, lastField = new FrontendMappingFieldDefinitionBuilder()
                .withMapping(lastField)
                .withAllowNull(false)
                .createFrontendMappingFieldDefinition()
        );
        validate(v -> (v instanceof String ? StringUtils.isNotEmpty((String)v) : v != null) ? null : "not.empty");
        return this;
    }

    public FormFieldDefinitionBuilder<V> warning() {
        fields.set(fields.size() - 1, lastField = new FrontendMappingFieldDefinitionBuilder()
                .withMapping(lastField)
                .withWarning(true)
                .withWarningMessage(null)
                .createFrontendMappingFieldDefinition()
        );
        return this;
    }

    @Autocomplete
    public FormFieldDefinitionBuilder<V> warning(String warning) {
        fields.set(fields.size() - 1, lastField = new FrontendMappingFieldDefinitionBuilder()
                .withMapping(lastField)
                .withWarning(true)
                .withWarningMessage(warning)
                .createFrontendMappingFieldDefinition()
        );
        return this;
    }

    @Autocomplete
    public FormFieldDefinitionBuilder<V> sectionWrapper() {
        fields.set(fields.size() - 1, lastField = new FrontendMappingFieldDefinitionBuilder()
                .withMapping(lastField)
                .withSectionWrapper(true)
                .createFrontendMappingFieldDefinition()
        );
        return this;
    }

    @Autocomplete
    public FormFieldDefinitionBuilder<V> section(String wrappingFieldName) {
        String sectionIdentifier = String.format("%s-div", wrappingFieldName);
        fields.set(fields.size() - 1, lastField = new FrontendMappingFieldDefinitionBuilder()
                .withMapping(lastField)
                .withSectionWrapped(true)
                .withSectionCssIdentifier(sectionIdentifier)
                .createFrontendMappingFieldDefinition()
        );
        return this;
    }

    @Autocomplete
    public FormFieldDefinitionBuilder<V> displayControl() {
        return sectionWrapper();
    }

    @Autocomplete
    public FormFieldDefinitionBuilder<V> displayWhenSelected(String wrappingFieldName) {
        String sectionIdentifier = wrappingFieldName.split("\\s+").length > 1
                ? wrappingFieldName.replaceAll("\\s+","-wrapped ") + "-wrapped "
                : String.format("%s-wrapped", wrappingFieldName);
        fields.set(fields.size() - 1, lastField = new FrontendMappingFieldDefinitionBuilder()
                .withMapping(lastField)
                .withSectionWrapped(true)
                .withSectionCssIdentifier(sectionIdentifier)
                .createFrontendMappingFieldDefinition()
        );
        return this;
    }

    @Autocomplete
    public FormFieldDefinitionBuilder<V> style(String style) {
        fields.set(fields.size() - 1, lastField = new FrontendMappingFieldDefinitionBuilder()
                .withMapping(lastField)
                .withStyle(style)
                .createFrontendMappingFieldDefinition()
        );
        return this;
    }

    @Autocomplete
    public FormFieldDefinitionBuilder<V> displayWhenSelected(String wrappingFieldName, String value) {
        String sectionIdentifier = wrappingFieldName.split("\\s+").length > 1
                ? wrappingFieldName.replaceAll("\\s+","-wrapped ") + "-wrapped " + value
                : String.format("%s-wrapped %s", wrappingFieldName, value);
        fields.set(fields.size() - 1, lastField = new FrontendMappingFieldDefinitionBuilder()
                .withMapping(lastField)
                .withSectionWrapped(true)
                .withSectionCssIdentifier(sectionIdentifier)
                .createFrontendMappingFieldDefinition()
        );
        return this;
    }

    @Autocomplete
    public FormFieldDefinitionBuilder<V> datalistValues(Object datalistValues) {
        if(datalistValues instanceof String) {
            fields.set(fields.size() - 1, lastField = new FrontendMappingFieldDefinitionBuilder()
                    .withMapping(lastField)
                    .withDatalistId((String)datalistValues)
                    .createFrontendMappingFieldDefinition()
            );
        } else if (datalistValues instanceof List) {
            fields.set(fields.size() - 1, lastField = new FrontendMappingFieldDefinitionBuilder()
                    .withMapping(lastField)
                    .withDatalistId(lastField.getName())
                    .withDatalistSupplier((f, d) -> d.collectionToLinkedMap((List)datalistValues))
                    .createFrontendMappingFieldDefinition()
            );
        } else if (datalistValues instanceof Map) {
            fields.set(fields.size() - 1, lastField = new FrontendMappingFieldDefinitionBuilder()
                    .withMapping(lastField)
                    .withDatalistId(lastField.getName())
                    .withDatalistSupplier((f, d) -> datalistValues)
                    .createFrontendMappingFieldDefinition()
            );
        }
        return this;
    }

    @Autocomplete
    public FormFieldDefinitionBuilder<V> repositorySupplier(Function<SecureEntityDictionaryRepository, Object> datalistSupplier) {
        fields.set(fields.size() - 1, lastField = new FrontendMappingFieldDefinitionBuilder()
                .withMapping(lastField)
                .withDatalistSupplier((f, d) -> datalistSupplier.apply(d))
                .createFrontendMappingFieldDefinition()
        );
        return this;
    }

    @Autocomplete
    public FormFieldDefinitionBuilder<V> formAndRepositorySupplier(BiFunction<DtoAndEntity, SecureEntityDictionaryRepository, Object> datalistSupplier) {
        fields.set(fields.size() - 1, lastField = new FrontendMappingFieldDefinitionBuilder()
                .withMapping(lastField)
                .withDatalistSupplier(datalistSupplier)
                .withFormBasedDatalistSupplier(true)
                .createFrontendMappingFieldDefinition()
        );
        return this;
    }

    @Autocomplete
    public FormFieldDefinitionBuilder<V> multiselect() {
        fields.set(fields.size() - 1, lastField = new FrontendMappingFieldDefinitionBuilder()
                .withMapping(lastField)
                .withType(lastField.type.equals(FieldType.checkbox) ? FieldType.checkbox_list : lastField.type)
                .withMultiselect(true)
                .createFrontendMappingFieldDefinition()
        );
        return this;
    }

    @Autocomplete(doc = "One form row has up to 12 columns. " +
            "<br/>Column parameter indicates the index of the start column for the field in a row." +
            "<br/>Width parameter defines the width of the field expressed in a number of columns.")
    public FormFieldDefinitionBuilder<V> position(int column, int width) {
        fields.set(fields.size() - 1, lastField = new FrontendMappingFieldDefinitionBuilder()
                .withMapping(lastField)
                .withColumn(column)
                .withWidth(width)
                .createFrontendMappingFieldDefinition()
        );
        return this;
    }

    @Autocomplete(doc = "Appends last defined field to the previous field's row in form.")
    public FormFieldDefinitionBuilder<V> previousRow() {
        fields.set(fields.size() - 1, lastField = new FrontendMappingFieldDefinitionBuilder()
                .withMapping(lastField)
                .withAppendToPreviousRow()
                .createFrontendMappingFieldDefinition()
        );
        return this;
    }

    @Autocomplete
    public FormFieldDefinitionBuilder<V> disable() {
        return enabled((organizationUser, longIdEntity) -> false);
    }

    public <VT> FormFieldDefinitionBuilder<VT> valueType(Class<VT> c) {
        return (FormFieldDefinitionBuilder<VT>)this;
    }

}