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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.openkoda.model.OpenkodaModule;
import com.openkoda.model.Organization;
import com.openkoda.model.PrivilegeBase;
import com.openkoda.repository.SecureEntityDictionaryRepository;
import com.openkoda.uicomponent.annotation.Autocomplete;
import reactor.util.function.Tuple2;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.openkoda.core.form.FieldType.*;

public class FormFieldDefinitionBuilderStart {
    public static final String DATALIST_PREFIX = "__datalist_";
    protected final List<FrontendMappingFieldDefinition> fields = new ArrayList<>();
    protected final List<Tuple2<FrontendMappingFieldDefinition, Function<?, String>>> fieldValidators = new ArrayList<>();
    protected final List<Function<? extends Form, Map<String, String>>>  formValidators = new ArrayList<>();
    protected String entityDescriptionSqlFormula;
    protected final String formName;
    protected final PrivilegeBase defaultReadPrivilege;
    protected final PrivilegeBase defaultWritePrivilege;
    protected static String RECAPTCHA = "reCaptcha";

    @JsonIgnore
    protected FrontendMappingFieldDefinition lastField;

    public FormFieldDefinitionBuilderStart (
            String formName,
            PrivilegeBase defaultReadPrivilege,
            PrivilegeBase defaultWritePrivilege) {
        this.formName = formName;
        this.defaultReadPrivilege = defaultReadPrivilege;
        this.defaultWritePrivilege = defaultWritePrivilege;
    }

    @Autocomplete(doc = """
            Create list of values which can be later used to populate e.g. dropdowns. (Presentation layer impact only). Examples:
            <br/>Simple data list with fixed values:<br/>
            <code>
                .datalist("weekendDays").repositorySupplier(d => d.toLinkedMap(["Saturday","Sunday"]))
                .dropdown("nonWorking", "weekendDays")
            </code>
            <br/>Simple data list with fixed values:<br/>
            <code>
            .datalist("workingDays").datalistValues(["Mon","Tue"])
            .dropdown("working", "workingDays")
            <code>
            """)
    public FormFieldDefinitionBuilder<Object> datalist(String datalistId) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, DATALIST_PREFIX + datalistId, datalist, defaultReadPrivilege, defaultWritePrivilege)
                .withDatalistId(datalistId)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<Object>)this;
    }

    @Autocomplete(doc = """
            Create string column in the database and add simple text input to the form. Examples:
            <br/>Simple text input both in the form and in the table:<br/>
            <code>
            .text("firstName")
            </code>
            <br/>Simple text calculated from sql formula:<br/>
            <code>
            .checkbox("username").sqlFormula("select first_name ||' '|| last_name from customer")
            </code>
            """)
    public FormFieldDefinitionBuilder<String> text(String fieldName) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, text, defaultReadPrivilege, defaultWritePrivilege)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<String>)this;
    }
    @Autocomplete(doc = """
            Create string column in the database and add textarea input to the form. Examples:
            <br/>Simple textarea input both in the form and in the table:<br/>
            <code>
            .textarea("notes")
            </code>
            """)
    public FormFieldDefinitionBuilder<String> textarea(String fieldName) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, textarea, defaultReadPrivilege, defaultWritePrivilege)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<String>)this;
    }
    @Autocomplete(doc = """
            Create boolean column in the database and add checkbox input to the form. Examples:
            <br/>Simple checkbox both in the form and in the table:<br/>
            <code>
            .checkbox("flag")
            </code>
            <br/>Simple checkbox calculated from sql formula:<br/>
            <code>
            .checkbox("flag").sqlFormula("true")
            </code>
            """)
    public FormFieldDefinitionBuilder<Boolean> checkbox(String fieldName) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, checkbox, defaultReadPrivilege, defaultWritePrivilege)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<Boolean>)this;
    }
    @Autocomplete(doc = """
            Create date with time column in the database and add date time picker input to the form. Examples:
            <br/>Simple datetime both in the form and in the table:<br/>
            <code>
            .datetime("busArrivalDateTime")
            </code>
            <br/>Simple datetime calculated from sql formula:<br/>
            <code>
            .datetime("currentTime").sqlFormula("select CURRENT_TIME")
            </code>
            """)
    public FormFieldDefinitionBuilder<LocalDateTime> datetime(String fieldName) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, datetime, defaultReadPrivilege, defaultWritePrivilege)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<LocalDateTime>)this;
    }
    @Autocomplete(doc = """
            Create date column in the database and add date picker input to the form. Examples:
            <br/>Simple date both in the form and in the table:<br/>
            <code>
            .date("busArrivalDate")
            </code>
            <br/>Simple date calculated from sql formula:<br/>
            <code>
            .date("currentDate").sqlFormula("select CURRENT_DATE")
            </code>
            """)
    public FormFieldDefinitionBuilder<LocalDate> date(String fieldName) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, date, defaultReadPrivilege, defaultWritePrivilege)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<LocalDate>)this;
    }
    @Autocomplete(doc = """
            Create numeric column in the database and add numeric input to the form. Examples:
            <br/>Simple numeric both in the form and in the table:<br/>
            <code>
            .number(455)
            </code>
            <br/>Simple numeric calculated from sql formula:<br/>
            <code>
            .number("quantity").sqlFormula("select count(*) from products")
            </code>
            """)
    public FormFieldDefinitionBuilder<Number> number(String fieldName) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, number, defaultReadPrivilege, defaultWritePrivilege)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<Number>)this;
    }

    @Autocomplete(doc = "Create non null string column in the database and select input with required value on presentation layer." +
            "This action may be preceded by appropriate data list creation. See also 'datalist'")
    public FormFieldDefinitionBuilder<String> dropdown(String fieldName) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, dropdown, defaultReadPrivilege, defaultWritePrivilege)
                .withDatalistId(fieldName)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<String>)this;
    }

    @Autocomplete(doc = "Create nullable numeric column in the database and a dropdown element on the presentation layer." +
            "Values available in the dropdown are loaded from the database as the referenced entity key table records. ")
    public FormFieldDefinitionBuilder<Long> manyToOne(String fieldName, String referencedEntityKey) {
        String datalistId = fieldName + "_" + referencedEntityKey;
        datalist(datalistId, d -> d.dictionary(referencedEntityKey));
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, many_to_one, defaultReadPrivilege, defaultWritePrivilege)
                .withDatalistId(datalistId)
                .withAllowNull(true)
                .withReferencedEntityKey(referencedEntityKey)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<Long>)this;
    }

    @Autocomplete(doc = "Create a multiselect dropdown element on the presentation layer." +
            "Values available in the dropdown are loaded from the database as the referenced entity key table records. ")
    public FormFieldDefinitionBuilder<Long> oneToMany(String fieldName, String referencedEntityKey, String mappedByFieldName) {
// gigamerge: update many to one for new builder
//        fields.add(lastField = createFormFieldDefinition(formName, fieldName, 0, referencedEntityKey, one_to_many, defaultReadPrivilege, defaultWritePrivilege));
        String datalistId = fieldName + "_" + referencedEntityKey;
        datalist(datalistId, d -> d.dictionary(referencedEntityKey));
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, one_to_many, defaultReadPrivilege, defaultWritePrivilege)
                .withDatalistId(datalistId)
                .withAllowNull(true)
                .withReferencedEntityKey(referencedEntityKey)
                .withMultiselect(true)
                .withMappedByFieldName(mappedByFieldName)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<Long>)this;
    }

    @Autocomplete(doc = "Create nullable numeric reference column in the database and select input populated with organization IDs on presentation layer.")
    public FormFieldDefinitionBuilder<Long> organizationSelect(String fieldName) {
        datalist("organizations", d -> d.dictionary(Organization.class));
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, organization_select, defaultReadPrivilege, defaultWritePrivilege)
                .withDatalistId("organizations")
                .withAllowNull(true)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<Long>)this;
    }

    @Autocomplete(doc = "Create nullable numeric reference column in the database and select input populated with Openkoda Modules IDs on presentation layer.")
    public FormFieldDefinitionBuilder<Long> moduleSelect(String fieldName) {
        datalist("modules", d -> d.dictionary(OpenkodaModule.class));
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, module_select, defaultReadPrivilege, defaultWritePrivilege)
                .withDatalistId("modules")
                .withAllowNull(true)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<Long>)this;
    }

    @Autocomplete(doc = "Create radio element.")
    public FormFieldDefinitionBuilder<Object> radio(String fieldName) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, radio, defaultReadPrivilege, defaultWritePrivilege)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<Object>)this;
    }

    @Autocomplete(doc = "Create divider element only on presentation layer.")
    public FormFieldDefinitionBuilder<Object> divider(String fieldName) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, divider, defaultReadPrivilege, defaultWritePrivilege)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<Object>)this;
    }

    @Autocomplete(doc = "Create collapsable element only on presentation layer.")
    public FormFieldDefinitionBuilder<Object> collapsable(String fieldName) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, caret_down, defaultReadPrivilege, defaultWritePrivilege)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<Object>)this;
    }

    @Autocomplete(doc = "Create non null long string column in the database and a HTML code editor element on presentation layer by default.")
    public FormFieldDefinitionBuilder<String> code(String fieldName) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, code_html, defaultReadPrivilege, defaultWritePrivilege)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<String>)this;
    }

    @Autocomplete(doc = "Create non null string column in the database and an input of type hidden on presentation layer.")
    public FormFieldDefinitionBuilder<String> hidden(String fieldName) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, hidden, defaultReadPrivilege, defaultWritePrivilege)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<String>)this;
    }

    @Autocomplete(doc = "Create non null boolean column in the database and toggle element on presentation layer.")
    public FormFieldDefinitionBuilder<Object> toggle(String fieldName) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, switch_values, defaultReadPrivilege, defaultWritePrivilege)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<Object>)this;
    }

    @Autocomplete(doc = "Create non null string column in the database and password input element on presentation layer.")
    public FormFieldDefinitionBuilder<String> password(String fieldName) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, password, defaultReadPrivilege, defaultWritePrivilege)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<String>)this;
    }

    @Autocomplete(doc = "Create non null string column in the database and map element on presentation layer.")
    public FormFieldDefinitionBuilder<Object> map(String fieldName) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, map, defaultReadPrivilege, defaultWritePrivilege)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<Object>) this;
    }

    @Autocomplete(doc = "Create non null string column in the database to store comma-separated list of file IDs and a file gallery with upload section on presentation layer.")
    public FormFieldDefinitionBuilder<Object> file(String fieldName, String mimeType) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, files, defaultReadPrivilege, defaultWritePrivilege)
                .withReferencedEntityKey("file")
                .withDatalistId(fieldName)
                .withDatalistSupplier(null)
                .withContentType(mimeType)
                .withDtoToEntityValueConverter(filesConverter)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<Object>)this;
    }

    @Autocomplete(doc = "Create single image selector element (only on presentation layer).")
    public FormFieldDefinitionBuilder<Object> image(String fieldName) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, image, defaultReadPrivilege, defaultWritePrivilege)
                .withContentType("image/png,image/jpeg")
                .withDtoToEntityValueConverter(filesConverter)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<Object>)this;
    }

    @Autocomplete(doc = "Create non null string column in the database and color picker element on presentation layer.")
    public FormFieldDefinitionBuilder<String> color(String fieldName) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, color_picker, defaultReadPrivilege, defaultWritePrivilege)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<String>)this;
    }

    @Autocomplete(doc = "Create non null timestamp with timezone column in the database and time picker element on presentation layer.")
    public FormFieldDefinitionBuilder<Object> time(String fieldName) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, time, defaultReadPrivilege, defaultWritePrivilege)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<Object>)this;
    }

    @Autocomplete(doc = "Create recaptcha element only on presentation layer.")
    public FormFieldDefinitionBuilder<Object> recaptcha() {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, RECAPTCHA, recaptcha, defaultReadPrivilege, defaultWritePrivilege)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<Object>)this;
    }

    @Autocomplete(doc = "Create div only on presentation layer.")
    public FormFieldDefinitionBuilder<Object> div(String fieldName) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, div, defaultReadPrivilege, defaultWritePrivilege)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<Object>)this;
    }

    @Autocomplete(doc = "Create custom fragment form element.")
    public FormFieldDefinitionBuilder<Object> custom(String fieldName, String fragmentName) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, custom, defaultReadPrivilege, defaultWritePrivilege)
                .withFragmentName(fragmentName)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<Object>)this;
    }

    @Autocomplete
    public FormFieldDefinitionBuilder<Object> formDescriptionSqlFormula(String sqlFormula) {
        this.entityDescriptionSqlFormula = sqlFormula;
        return (FormFieldDefinitionBuilder<Object>)this;
    }


    //    hidden in autocomplete
    public FormFieldDefinitionBuilder<Object> ruleThen(String fieldName, BiFunction<DtoAndEntity, SecureEntityDictionaryRepository, Object> datalistSupplier, String url) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, rule_then, defaultReadPrivilege, defaultWritePrivilege)
                .withUrl(url)
                .withDatalistSupplier(datalistSupplier)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<Object>)this;
    }

//    hidden in autocomplete
    public FormFieldDefinitionBuilder<Object> customFieldType(String fieldName, Function<Object, FieldType> fieldTypeFunction) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, customFieldType, defaultReadPrivilege, defaultWritePrivilege)
                .withFieldTypeFunction(fieldTypeFunction)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<Object>)this;
    }

//    hidden in autocomplete
    public FormFieldDefinitionBuilder<Object> submitToNewTab(String fieldName, String url) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, submit_to_new_tab, defaultReadPrivilege, defaultWritePrivilege)
                        .withUrl(url)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<Object>)this;
    }

//    hidden in autocomplete
    public FormFieldDefinitionBuilder<String> checkboxListGrouped(String fieldName, String datalistId) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, checkbox_list_grouped, defaultReadPrivilege, defaultWritePrivilege)
                .withDatalistId(datalistId)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<String>)this;
    }

//    DEPRECATED, REPLACED BY NEW FORM API

    @Deprecated
    public FormFieldDefinitionBuilder<String> imageUrl(String fieldName) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, image_url, defaultReadPrivilege, defaultWritePrivilege)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<String>)this;
    }

    @Deprecated
    public FormFieldDefinitionBuilder<Object> datalist(String datalistId, BiFunction<DtoAndEntity, SecureEntityDictionaryRepository, Object> datalistSupplier) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, DATALIST_PREFIX + datalistId, datalist, defaultReadPrivilege, defaultWritePrivilege)
                .withDatalistId(datalistId)
                .withDatalistSupplier(datalistSupplier)
                .withFormBasedDatalistSupplier(true)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<Object>)this;
    }

    @Deprecated
    public FormFieldDefinitionBuilder<Object> datalist(String datalistId, Function<SecureEntityDictionaryRepository, Object> datalistSupplier) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, DATALIST_PREFIX + datalistId, datalist, defaultReadPrivilege, defaultWritePrivilege)
                .withDatalistId(datalistId)
                .withDatalistSupplier((f, d) -> datalistSupplier.apply(d))
                .withFormBasedDatalistSupplier(false)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<Object>)this;
    }

    @Deprecated
    public FormFieldDefinitionBuilder<String> dropdown(String fieldName, String datalistId, boolean allowNull) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, dropdown, defaultReadPrivilege, defaultWritePrivilege)
                .withDatalistId(datalistId)
                .withAllowNull(allowNull)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<String>)this;
    }

    @Deprecated
    public FormFieldDefinitionBuilder<String> dropdown(String fieldName, String datalistId, Boolean allowNull) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, dropdown, defaultReadPrivilege, defaultWritePrivilege)
                .withDatalistId(datalistId)
                .withAllowNull(allowNull)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<String>)this;
    }

    @Deprecated
    public FormFieldDefinitionBuilder<String> dropdownWithDisable(String fieldName, String datalistId) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, dropdown_with_disable, defaultReadPrivilege, defaultWritePrivilege)
                .withDatalistId(datalistId)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<String>)this;
    }

    @Deprecated
    public FormFieldDefinitionBuilder<String> dropdownWithDisable(String fieldName, BiFunction<DtoAndEntity, SecureEntityDictionaryRepository, Object> datalistSupplier) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, dropdown_with_disable, defaultReadPrivilege, defaultWritePrivilege)
                .withDatalistId(null)
                .withDatalistSupplier(datalistSupplier)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<String>)this;
    }

    @Deprecated
    public FormFieldDefinitionBuilder<String> sectionWithDropdown(String fieldName, String datalistId) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, section_with_dropdown, defaultReadPrivilege, defaultWritePrivilege)
                .withDatalistId(datalistId)
                .withAllowNull(true)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<String>)this;
    }

    @Deprecated
    public FormFieldDefinitionBuilder<String> sectionWithDropdown(String fieldName, BiFunction<DtoAndEntity, SecureEntityDictionaryRepository, Object> datalistSupplier) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, section_with_dropdown, defaultReadPrivilege, defaultWritePrivilege)
                .withAllowNull(true)
                .withDatalistSupplier(datalistSupplier)
                .withFormBasedDatalistSupplier(true)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<String>)this;
    }

    @Deprecated
    public FormFieldDefinitionBuilder<String> dropdownNonDto(String fieldName, String datalistId) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, dropdown, defaultReadPrivilege, defaultWritePrivilege)
                .withDatalistId(datalistId)
                .withNonDto(true)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<String>)this;
    }
    @Deprecated
    public FormFieldDefinitionBuilder<Object> radioList(String fieldName, String datalistId) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, radio_list, defaultReadPrivilege, defaultWritePrivilege)
                .withDatalistId(datalistId)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<Object>)this;
    }

    @Deprecated
    public FormFieldDefinitionBuilder<Object> radioListNoLabel(String fieldName, BiFunction<DtoAndEntity, SecureEntityDictionaryRepository, Object> datalistSupplier) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, radio_list_no_label, defaultReadPrivilege, defaultWritePrivilege)
                .withDatalistSupplier(datalistSupplier)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<Object>)this;
    }

    @Deprecated
    public FormFieldDefinitionBuilder<Object> radioListNoLabel(String fieldName, String dataListId) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, radio_list_no_label, defaultReadPrivilege, defaultWritePrivilege)
                        .withDatalistId(dataListId)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<Object>)this;
    }


    @Deprecated
    public FormFieldDefinitionBuilder<String> codeCss(String fieldName) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, code_css, defaultReadPrivilege, defaultWritePrivilege)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<String>)this;
    }

    @Deprecated
    public FormFieldDefinitionBuilder<String> codeHtml(String fieldName) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, code_html, defaultReadPrivilege, defaultWritePrivilege)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<String>)this;
    }

    @Deprecated
    public FormFieldDefinitionBuilder<String> codeJs(String fieldName) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, code_js, defaultReadPrivilege, defaultWritePrivilege)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<String>)this;
    }

    @Deprecated
    public FormFieldDefinitionBuilder<String> codeWithWebendpointAutocomplete(String fieldName) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, code_with_webendpoint_autocomplete, defaultReadPrivilege, defaultWritePrivilege)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<String>)this;
    }

    @Deprecated
    public FormFieldDefinitionBuilder<String> codeWithFormAutocomplete(String fieldName) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, code_with_form_autocomplete, defaultReadPrivilege, defaultWritePrivilege)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<String>)this;
    }


    @Deprecated
    public FormFieldDefinitionBuilder<Object> switchValuesWithWarning(String fieldName) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, switch_values_with_warning, defaultReadPrivilege, defaultWritePrivilege)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<Object>)this;
    }

    @Deprecated
    public FormFieldDefinitionBuilder<Object> sectionWithCheckboxWithWarning(String fieldName) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, section_with_checkbox_with_warning, defaultReadPrivilege, defaultWritePrivilege)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<Object>)this;
    }

    @Deprecated
    public FormFieldDefinitionBuilder<Object> sectionWithLink(String fieldName) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, section_with_link, defaultReadPrivilege, defaultWritePrivilege)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<Object>)this;
    }

    @Deprecated
    public FormFieldDefinitionBuilder<Object> sectionWithCheckbox(String fieldName) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, section_with_checkbox, defaultReadPrivilege, defaultWritePrivilege)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<Object>)this;
    }

    @Deprecated
    public FormFieldDefinitionBuilder<Object> sectionWithSwitch(String fieldName) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, section_with_switch, defaultReadPrivilege, defaultWritePrivilege)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<Object>)this;
    }

    @Deprecated
    public FormFieldDefinitionBuilder<Object> sectionWithSwitchContent(String fieldName) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, section_with_switch_content, defaultReadPrivilege, defaultWritePrivilege)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<Object>)this;
    }

    @Deprecated
    public FormFieldDefinitionBuilder<Object> imagesLibrary(String fieldName, BiFunction<DtoAndEntity, SecureEntityDictionaryRepository, Object> datalistSupplier) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, files_library, defaultReadPrivilege, defaultWritePrivilege)
                        .withDatalistId(fieldName)
                        .withDatalistSupplier(datalistSupplier)
                        .withContentType("image/png,image/jpeg")
                        .withDtoToEntityValueConverter(filesConverter)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<Object>)this;
    }

    @Deprecated
    public FormFieldDefinitionBuilder<Object> imageLibrary(String fieldName, BiFunction<DtoAndEntity, SecureEntityDictionaryRepository, Object> datalistSupplier) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, file_library, defaultReadPrivilege, defaultWritePrivilege)
                .withDatalistId(fieldName)
                .withDatalistSupplier(datalistSupplier)
                .withContentType("image/png,image/jpeg")
                .withDtoToEntityValueConverter(filesConverter)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<Object>)this;
    }

    @Deprecated
    public FormFieldDefinitionBuilder<Object> files(String fieldName, BiFunction<DtoAndEntity, SecureEntityDictionaryRepository, Object> datalistSupplier, String mimeType) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, files, defaultReadPrivilege, defaultWritePrivilege)
                .withDatalistId(fieldName)
                .withDatalistSupplier(datalistSupplier)
                .withContentType(mimeType)
                .withDtoToEntityValueConverter(filesConverter)
                .withFormBasedDatalistSupplier(true)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<Object>)this;
    }

    @Deprecated
    public FormFieldDefinitionBuilder<String> dropdown(String fieldName, Object datalist) {
        if(datalist instanceof String) {
            fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                    .createFrontendMappingDefinitionBase(formName, fieldName, dropdown, defaultReadPrivilege, defaultWritePrivilege)
                    .withDatalistId((String) datalist)
                    .createFrontendMappingFieldDefinition());
        } else if (datalist instanceof List) {
            fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                    .createFrontendMappingDefinitionBase(formName, fieldName, dropdown, defaultReadPrivilege, defaultWritePrivilege)
                    .withDatalistId(fieldName)
                    .withDatalistSupplier((f, d) -> SecureEntityDictionaryRepository.collectionToLinkedMap((List)datalist))
                    .withFormBasedDatalistSupplier(false)
                    .createFrontendMappingFieldDefinition());
        } else if (datalist instanceof Map) {
            fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                    .createFrontendMappingDefinitionBase(formName, fieldName, dropdown, defaultReadPrivilege, defaultWritePrivilege)
                    .withDatalistId(fieldName)
                    .withDatalistSupplier((f, d) -> datalist)
                    .withFormBasedDatalistSupplier(false)
                    .createFrontendMappingFieldDefinition());
        }
        return (FormFieldDefinitionBuilder<String>)this;
    }

    @Deprecated
    public FormFieldDefinitionBuilder<String> multiselect(String fieldName, String datalistId) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, multiselect, defaultReadPrivilege, defaultWritePrivilege)
                .withDatalistId(datalistId)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<String>)this;
    }

    @Deprecated
    public FormFieldDefinitionBuilder<String> checkboxList(String fieldName, BiFunction<DtoAndEntity, SecureEntityDictionaryRepository, Object> datalistSupplier) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, checkbox_list, defaultReadPrivilege, defaultWritePrivilege)
                .withDatalistId(fieldName)
                .withDatalistSupplier(datalistSupplier)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<String>)this;
    }

    @Deprecated
    public FormFieldDefinitionBuilder<String> checkboxList(String fieldName, String datalistId) {
        fields.add(lastField = new FrontendMappingFieldDefinitionBuilder()
                .createFrontendMappingDefinitionBase(formName, fieldName, checkbox_list, defaultReadPrivilege, defaultWritePrivilege)
                .withDatalistId(datalistId)
                .createFrontendMappingFieldDefinition());
        return (FormFieldDefinitionBuilder<String>)this;
    }

    @Deprecated
    public FormFieldDefinitionBuilder<Object> timePicker(String fieldName) {
        return time(fieldName);
    }

    //TODO: move to some better place
    Function filesConverter = new Function() {
        @Override
        public Object apply(Object o) {
            List<Long> result = new ArrayList<>();
            if (o == null) {return result;}
            if (o instanceof String) {
                result.add(Long.valueOf((String) o));
                return result;
            }
            if (o.getClass().isArray()) {
                String[] oa = (String[])o;
                for (String s : oa) {
                    result.add(Long.valueOf(s));
                }
                return result;
            }
            return result;
        }
    };
}