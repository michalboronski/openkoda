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

import com.openkoda.core.helper.Messages;
import com.openkoda.core.security.OrganizationUser;
import com.openkoda.model.PrivilegeBase;
import com.openkoda.model.common.LongIdEntity;
import com.openkoda.repository.SecureEntityDictionaryRepository;
import org.apache.commons.lang3.StringUtils;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.openkoda.core.form.FieldType.*;
import static com.openkoda.core.form.FormFieldDefinitionBuilderStart.DATALIST_PREFIX;

/**
 * <p>FormFieldDefinition class.</p>
 *
 * @author Arkadiusz Drysch (adrysch@stratoflow.com)
 * 
 */
public class FrontendMappingFieldDefinition {
    public final String formName;
    private final String name;
    public final String fragmentName;
    public final FieldType type;
    public final PrivilegeBase readPrivilege;
    public final PrivilegeBase writePrivilege;
    public final BiFunction<OrganizationUser, LongIdEntity, Boolean> canReadCheck;
    public final BiFunction<OrganizationUser, LongIdEntity, Boolean> canWriteCheck;
    public final BiFunction<DtoAndEntity, SecureEntityDictionaryRepository, Object> datalistSupplier;
    public final boolean formBasedDatalistSupplier;
    public final Function<AbstractForm, Object> valueSupplier;
    public final Function<Object, FieldType> fieldTypeFunction;
    private final static Function<Object, FieldType> nullFunction = a -> null;
    private final static Function<Object, Object> nullSupplier = null;
    public final String key;
    public final String labelKey;
    public final String placeholderKey;
    public final String tooltipKey;
    public final String warningKey;
    public final String alertKey;
    public final String descriptionKey;
    public final String datalistId;
    public final String preselectedValue;
    public final String autocomplete;

    public final String additionalCss;
    public final boolean allowNull;
    public final String url;
    public final String contentType;
    
    public final Function dtoToEntityValueConverter;
    public final Function entityToDtoValueConverter;

    public final PrivilegeBase actionPrivilege;
    public final String actionUrl;
    public final String actionLabelKey;
    public final boolean nonDto;
    
    public final String sqlFormula;
    public final String referencedEntityKey;
    
    // used when Privilege checks are used in order to hide or disable a field despite usually a global settings
    public final boolean strictWriteAccess;
    public final boolean strictReadAccess;
    public final boolean searchEnabled;
    public final boolean warning;
    public final String warningMessage;
    public final boolean sectionWrapper;
    public final boolean sectionWrapped;
    public final String sectionCssIdentifier;
    public final boolean multiselect;

//    form fields positioning attributes
    public final Integer column;
    public final Integer width;
    public final boolean appendToPreviousRow;
    public final String style;

//   reference related fields
    public final String mappedByFieldName;
    public final String descriptionSource;

    public boolean isText(Form form) {
        return getFieldType(form) == text;
    }

    public boolean isPassword(Form form) {
        return getFieldType(form) == password;
    }

    public boolean isHidden(Form form) {
        return getFieldType(form) == hidden;
    }

    public boolean isMap(Form form) { return getFieldType(form) == map; }

    public boolean isFileUpload(Form form) {
        FieldType t = getFieldType(form);
        return t == file_library || t == files_library || t == files || t == image;}

    public boolean isColorPicker(Form form) { return getFieldType(form) == color_picker; }

    public boolean isTimePicker(Form form) { return getFieldType(form) == time; }

    public boolean isDocumentEditor(Form form) { return getFieldType(form) == document; }

    public boolean isCodeEditor(Form form) {
        FieldType t = getFieldType(form);
        return t == code_js || t == code_html || t == code_css || t == code_with_webendpoint_autocomplete || t== code_with_form_autocomplete;
    }
    public boolean isCodeEditorWithWebendpointAutocomplete(Form form) {
        FieldType t = getFieldType(form);
        return t == code_with_webendpoint_autocomplete;
    }
    public boolean isCodeEditorWithFormAutocomplete(Form form) {
        FieldType t = getFieldType(form);
        return t == code_with_form_autocomplete;
    }
    public boolean isReCaptcha(Form form) { return getFieldType(form) == recaptcha; }

    public FieldType getFieldType(Form form) {
        return fieldTypeFunction != null ? fieldTypeFunction.apply(form) : type;
    }

    public FieldType getType() {
        return type;
    }

    public String getAdditionalCss() {
        return (sectionWrapped ? sectionCssIdentifier + " " : "") + (additionalCss != null ? additionalCss : "");
    }

    public String getLabel() {
        return Messages.fieldLabel(labelKey, getPlainName());
    }

    public String getTooltip() {
        return Messages.fieldTooltip(tooltipKey);
    }

    public String getPlaceholder() {
        return Messages.fieldPlaceholder(placeholderKey, getPlainName());
    }

    public String getWarning() {
        return warning ? (StringUtils.isNoneBlank(warningMessage) ? warningMessage : Messages.message(warningKey)) : null;
    }

    public String getName() {
        return name;
    }

    public String getPlainName() {
        return name;
    }

    public String getValueName() {
        return type == many_to_one || type == files ? name + "Id" : name;
    }
    
    public String getColumnName() {
        return type == many_to_one || type == files ? String.format("%s_id", name) : name;
    }

    public String getName(boolean dtoIsMap) {
        String valueName = getValueName();
        return nonDto ? valueName : (dtoIsMap ? "dto[" + valueName + "]" : "dto." + valueName );
    }

    public boolean isStrictWriteAccess() {
        return strictWriteAccess;
    }

    public boolean isStrictReadAccess() {
        return strictReadAccess;
    }
    
    protected FrontendMappingFieldDefinition(
            String formName,
            String name,
            String fragmentName,
            FieldType type,
            BiFunction<OrganizationUser, LongIdEntity, Boolean> canReadCheck,
            BiFunction<OrganizationUser, LongIdEntity, Boolean> canWriteCheck,
            PrivilegeBase readPrivilege,
            PrivilegeBase writePrivilege,
            Function<AbstractForm, Object> valueSupplier,
            BiFunction<DtoAndEntity, SecureEntityDictionaryRepository, Object> datalistSupplier,
            boolean formBasedDatalistSupplier,
            Function<Object, FieldType> fieldTypeFunction,
            String datalistId,
            String autocomplete,
            String additionalCss,
            boolean allowNull,
            String url,
            String contentType,
            Function<?, ?> dtoToEntityValueConverter,
            Function<?, ?> entityToDtoValueConverter,
            PrivilegeBase actionPrivilege,
            String actionUrl,
            String actionLabelKey,
            String sqlFormula,
            String referencedEntityKey,
            String preselectedValue,
            boolean nonDto,
            boolean strictReadAccess,
            boolean strictWriteAccess,
            boolean searchEnabled,
            boolean warning,
            String warningMessage,
            boolean sectionWrapper,
            boolean sectionWrapped,
            String sectionCssIdentifier,
            boolean multiselect,
            Integer column,
            Integer width,
            boolean appendToPreviousRow,
            String style,
            String mappedByFieldName,
            String descriptionSource) {
        this.formName = formName;
        this.name = name;
        this.fragmentName = fragmentName;
        this.type = type;
        this.readPrivilege = readPrivilege;
        this.writePrivilege = writePrivilege;
        this.canReadCheck = canReadCheck;
        this.canWriteCheck = canWriteCheck;
        this.valueSupplier = valueSupplier;
        this.datalistSupplier = datalistSupplier;
        this.formBasedDatalistSupplier = formBasedDatalistSupplier;
        this.fieldTypeFunction = fieldTypeFunction;
        this.autocomplete = autocomplete;
        this.nonDto = nonDto;
        this.strictReadAccess = strictReadAccess;
        this.strictWriteAccess = strictWriteAccess;
        this.warning = warning;
        this.warningMessage = warningMessage;
        this.sectionWrapper = sectionWrapper;
        this.sectionWrapped = sectionWrapped;
        this.sectionCssIdentifier = sectionCssIdentifier;
        this.multiselect = multiselect;
        this.key = formName + "." + name;
        this.labelKey = formName + "." + name + ".label";
        this.placeholderKey = formName + "." + name + ".placeholder";
        this.tooltipKey = formName + "." + name + ".tooltip";
        this.warningKey = formName + "." + name + ".warning";
        this.alertKey = formName + "." + name + ".alert";
        this.descriptionKey = formName + "." + name + ".description";
        this.datalistId = StringUtils.isNotEmpty(datalistId) ? (datalistId.contains(DATALIST_PREFIX) ? datalistId : DATALIST_PREFIX + datalistId) : null;
        this.additionalCss = additionalCss;
        this.allowNull = allowNull;
        this.url = url;
        this.contentType = contentType;
        this.dtoToEntityValueConverter = dtoToEntityValueConverter;
        this.entityToDtoValueConverter = entityToDtoValueConverter;
        this.actionPrivilege = actionPrivilege;
        this.actionUrl = actionUrl;
        this.actionLabelKey = actionLabelKey;
        this.sqlFormula = sqlFormula;
        this.referencedEntityKey = referencedEntityKey;
        this.searchEnabled = searchEnabled;
        this.preselectedValue = preselectedValue;
        this.column = column;
        this.width = width;
        this.appendToPreviousRow = appendToPreviousRow;
        this.style = style;
        this.mappedByFieldName = mappedByFieldName;
        this.descriptionSource = descriptionSource;
    }

}
