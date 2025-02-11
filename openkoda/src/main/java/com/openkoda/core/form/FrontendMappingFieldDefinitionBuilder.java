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

import com.openkoda.core.security.OrganizationUser;
import com.openkoda.model.PrivilegeBase;
import com.openkoda.model.common.LongIdEntity;
import com.openkoda.repository.SecureEntityDictionaryRepository;

import java.util.function.BiFunction;
import java.util.function.Function;

public class FrontendMappingFieldDefinitionBuilder {
    private String formName;
    private String name;
    private FieldType type;
    private BiFunction<OrganizationUser, LongIdEntity, Boolean> canReadCheck;
    private BiFunction<OrganizationUser, LongIdEntity, Boolean> canWriteCheck;
    private PrivilegeBase readPrivilege;
    private PrivilegeBase writePrivilege;
    private Function<AbstractForm, Object> valueSupplier;
    private BiFunction<DtoAndEntity, SecureEntityDictionaryRepository, Object> datalistSupplier;
    private boolean formBasedDatalistSupplier;
    private Function<Object, FieldType> fieldTypeFunction;
    private String datalistId;
    private String additionalCss;
    private boolean allowNull;
    private String url;
    private String fragmentName;
    private String contentType;
    private Function<?, ?> dtoToEntityValueConverter;
    private Function<?, ?> entityToDtoValueConverter;
    private PrivilegeBase actionPrivilege;
    private String actionUrl;
    private String actionLabelKey;
    private String sqlFormula;
    private String referencedEntityKey;
    public  String autocomplete;
    private String preselectedValue;
    private boolean nonDto;
    private boolean searchEnabled;
    public boolean warning;
    public String warningMessage;
    public boolean sectionWrapper;
    public boolean sectionWrapped;
    public String sectionCssIdentifier;
    public boolean multiselect;
    public Integer column;
    public Integer width;
    public boolean appendToPreviousRow = false;
    public boolean strictWriteAccess;
    public boolean strictReadAccess;
    public String style;
    public String mappedByFieldName;
    public String descriptionSource;

    public FrontendMappingFieldDefinitionBuilder withFormName(String formName) {
        this.formName = formName;
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withType(FieldType type) {
        this.type = type;
        this.fragmentName = type.getFragmentName();
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withCanReadCheck(BiFunction<OrganizationUser, LongIdEntity, Boolean> canReadCheck) {
        this.canReadCheck = canReadCheck;
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withCanWriteCheck(BiFunction<OrganizationUser, LongIdEntity, Boolean> canWriteCheck) {
        this.canWriteCheck = canWriteCheck;
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withReadPrivilege(PrivilegeBase readPrivilege) {
        this.readPrivilege = readPrivilege;
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withWritePrivilege(PrivilegeBase writePrivilege) {
        this.writePrivilege = writePrivilege;
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withValueSupplier(Function<AbstractForm, Object> valueSupplier) {
        this.valueSupplier = valueSupplier;
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withDatalistSupplier(BiFunction<DtoAndEntity, SecureEntityDictionaryRepository, Object> datalistSupplier) {
        this.datalistSupplier = datalistSupplier;
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withFormBasedDatalistSupplier(boolean formBasedDatalistSupplier) {
        this.formBasedDatalistSupplier = formBasedDatalistSupplier;
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withFieldTypeFunction(Function<Object, FieldType> fieldTypeFunction) {
        this.fieldTypeFunction = fieldTypeFunction;
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withDatalistId(String datalistId) {
        this.datalistId = datalistId;
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withAdditionalCss(String additionalCss) {
        this.additionalCss = additionalCss;
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withAllowNull(boolean allowNull) {
        this.allowNull = allowNull;
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withUrl(String url) {
        this.url = url;
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withFragmentName(String fragmentName) {
        this.fragmentName = fragmentName;
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withDtoToEntityValueConverter(Function<?, ?> dtoToEntityValueConverter) {
        this.dtoToEntityValueConverter = dtoToEntityValueConverter;
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withEntityToDtoValueConverter(Function<?, ?> entityToDtoValueConverter) {
        this.entityToDtoValueConverter = entityToDtoValueConverter;
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withActionPrivilege(PrivilegeBase actionPrivilege) {
        this.actionPrivilege = actionPrivilege;
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withActionLabelKey(String actionLabelKey) {
        this.actionLabelKey = actionLabelKey;
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withSqlFormula(String sqlFormula) {
        this.sqlFormula = sqlFormula;
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withReferencedEntityKey(String referencedEntityKey) {
        this.referencedEntityKey = referencedEntityKey;
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withPreselectedValue(String preselectedValue) {
        this.preselectedValue = preselectedValue;
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withNonDto(boolean nonDto) {
        this.nonDto = nonDto;
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withSearchEnabled(boolean searchEnabled) {
        this.searchEnabled = searchEnabled;
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withAutocomplete(String autocomplete) {
        this.autocomplete = autocomplete;
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withWarning(boolean warning) {
        this.warning = warning;
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withWarningMessage(String warningMessage) {
        this.warningMessage = warningMessage;
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withSectionWrapper(boolean sectionWrapper) {
        this.sectionWrapper = sectionWrapper;
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withSectionWrapped(boolean sectionWrapped) {
        this.sectionWrapped = sectionWrapped;
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withSectionCssIdentifier(String sectionCssIdentifier) {
        this.sectionCssIdentifier = sectionCssIdentifier;
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withMultiselect(boolean multiselect) {
        this.multiselect = multiselect;
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withColumn(Integer column) {
        this.column = column;
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withWidth(Integer width) {
        this.width = width;
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withAppendToPreviousRow(boolean appendToPreviousRow) {
        this.appendToPreviousRow = appendToPreviousRow;
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withAppendToPreviousRow() {
        this.appendToPreviousRow = true;
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withStrictWriteAccess(boolean strictWriteAccess) {
        this.strictWriteAccess = strictWriteAccess;
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withStrictReadAccess(boolean strictReadAccess) {
        this.strictReadAccess = strictReadAccess;
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withStyle(String style) {
        this.style = style;
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withMappedByFieldName(String mappedByFieldName) {
        this.mappedByFieldName = mappedByFieldName;
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder withDescriptionSource(String source) {
        this.descriptionSource = source;
        return this;
    }


    public FrontendMappingFieldDefinitionBuilder withMapping(FrontendMappingFieldDefinition f) {
        this.withFormName(f.formName)
                .withName(f.getName())
                .withType(f.type)
                .withCanReadCheck(f.canReadCheck)
                .withCanWriteCheck(f.canWriteCheck)
                .withReadPrivilege(f.readPrivilege)
                .withWritePrivilege(f.writePrivilege)
                .withValueSupplier(f.valueSupplier)
                .withDatalistSupplier(f.datalistSupplier)
                .withFormBasedDatalistSupplier(f.formBasedDatalistSupplier)
                .withFieldTypeFunction(f.fieldTypeFunction)
                .withDatalistId(f.datalistId)
                .withAdditionalCss(f.additionalCss)
                .withAllowNull(f.allowNull)
                .withUrl(f.url)
                .withFragmentName(f.fragmentName)
                .withContentType(f.contentType)
                .withDtoToEntityValueConverter(f.dtoToEntityValueConverter)
                .withEntityToDtoValueConverter(f.entityToDtoValueConverter)
                .withActionPrivilege(f.actionPrivilege)
                .withActionUrl(f.actionUrl)
                .withActionLabelKey(f.actionLabelKey)
                .withSqlFormula(f.sqlFormula)
                .withReferencedEntityKey(f.referencedEntityKey)
                .withPreselectedValue(f.preselectedValue)
                .withNonDto(f.nonDto)
                .withSearchEnabled(f.searchEnabled)
                .withAutocomplete(f.autocomplete)
                .withWarning(f.warning)
                .withWarningMessage(f.warningMessage)
                .withSectionWrapper(f.sectionWrapper)
                .withSectionWrapped(f.sectionWrapped)
                .withSectionCssIdentifier(f.sectionCssIdentifier)
                .withMultiselect(f.multiselect)
                .withColumn(f.column)
                .withWidth(f.width)
                .withAppendToPreviousRow(f.appendToPreviousRow)
                .withStrictReadAccess(f.strictReadAccess)
                .withStrictWriteAccess(f.strictWriteAccess)
                .withStyle(f.style)
                .withMappedByFieldName(f.mappedByFieldName)
                .withDescriptionSource(f.descriptionSource);
        return this;
    }

    public FrontendMappingFieldDefinitionBuilder createFrontendMappingDefinitionBase(String formName,
                                                                                     String fieldName,
                                                                                     FieldType fieldType,
                                                                                     PrivilegeBase defaultReadPrivilege,
                                                                                     PrivilegeBase defaultWritePrivilege) {
        return this
                .withFormName(formName)
                .withName(fieldName)
                .withType(fieldType)
                .withFragmentName(fieldType.getFragmentName())
                .withReadPrivilege(defaultReadPrivilege)
                .withWritePrivilege(defaultWritePrivilege);
    }

    public FrontendMappingFieldDefinition createFrontendMappingFieldDefinition() {
        return new FrontendMappingFieldDefinition(
                formName,
                name,
                fragmentName,
                type,
                canReadCheck,
                canWriteCheck,
                readPrivilege,
                writePrivilege,
                valueSupplier,
                datalistSupplier,
                formBasedDatalistSupplier,
                fieldTypeFunction,
                datalistId,
                autocomplete,
                additionalCss,
                allowNull,
                url,
                contentType,
                dtoToEntityValueConverter,
                entityToDtoValueConverter,
                actionPrivilege,
                actionUrl,
                actionLabelKey,
                sqlFormula,
                referencedEntityKey,
                preselectedValue,
                nonDto,
                strictReadAccess,
                strictWriteAccess,
                searchEnabled,
                warning,
                warningMessage,
                sectionWrapper,
                sectionWrapped,
                sectionCssIdentifier,
                multiselect,
                column,
                width,
                appendToPreviousRow,
                style,
                mappedByFieldName,
                descriptionSource
        );
    }

}