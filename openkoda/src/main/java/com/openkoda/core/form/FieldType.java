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

/**
 * Provides list of available form fields
 * All of these have their representation in the HTML form generator
 *
 * @author Arkadiusz Drysch (adrysch@stratoflow.com)
 *
 */
public enum FieldType {

    //Direct single value:
    text(FieldDbType.VARCHAR_255, "form-field::text"),
    password(FieldDbType.VARCHAR_255, "form-field::password"),
    hidden(FieldDbType.BIGINT,"form-field::hidden"),

    //date and time
    date(FieldDbType.DATE,"form-field::date"),
    datetime(FieldDbType.TIMESTAMP_W_TZ,"form-field::datetime"),
    time(FieldDbType.TIME_W_TZ, "form-field::timepicker"),

    //Direct single boolean value:
    checkbox(FieldDbType.BOOLEAN, "form-field::checkbox"),
    radio(FieldDbType.VARCHAR_255, "form-field::radio"),
    checkbox_with_warning(FieldDbType.BOOLEAN),
    switch_values(FieldDbType.BOOLEAN, "form-field::switch"),
    switch_values_with_warning(FieldDbType.BOOLEAN),

    //Simple textarea
    textarea(FieldDbType.VARCHAR_1000, "form-field::textarea"),

    //Code edition:
    code_html(FieldDbType.VARCHAR_262144, "form-field::code-html"),
    code_css(FieldDbType.VARCHAR_262144, "form-field::code-css"),
    code_js(FieldDbType.VARCHAR_262144, "form-field::code-js"),
    code_with_webendpoint_autocomplete(FieldDbType.VARCHAR_262144),
    code_with_form_autocomplete(FieldDbType.VARCHAR_262144),

    //foreign key reference
    many_to_one(FieldDbType.BIGINT, "form-field::many-to-one"),
    //Special for organization selection:
    organization_select(FieldDbType.BIGINT),
    module_select(FieldDbType.VARCHAR_255),

    datalist(false),
    //One value from dictionary:
    dropdown(FieldDbType.VARCHAR_255, "form-field::dropdown"),
    dropdown_with_disable,
    radio_list(FieldDbType.VARCHAR_255),
    radio_list_no_label,
    dropdown_with_entities,

    //Many values from dictionary:
    checkbox_list(FieldDbType.VARCHAR_1000, "form-field::checkbox-list"),
    checkbox_list_grouped,
    multiselect(FieldDbType.VARCHAR_1000),

    //Visual divider:
    divider("form-field::divider"),
    section_with_link,
    section_with_checkbox,
    section_with_checkbox_with_warning,
    section_with_switch,
    section_with_switch_content(false),

    //Buttons
    button,
    submit_to_new_tab(false),
    //

    number(FieldDbType.NUMERIC, "form-field::number"),
    map(FieldDbType.VARCHAR_255, "form-field::map"),

    document,
    rule_then, rule_then_else,

    image_url(FieldDbType.VARCHAR_255),

    //Files
    files_library, file_library,
    images_library, image_library,
    image("form-field::image"),
    files(FieldDbType.VARCHAR_255, "form-field::file"),

    //One to Many component
    one_to_many(true,"form-field::one-to-many"),
    //
    color_picker(FieldDbType.VARCHAR_255,"form-field::color"),

    section_with_dropdown(FieldDbType.VARCHAR_255),

    recaptcha("form-field::recaptcha"),
    div("form-field::simple-div"),
    caret_down("form-field::caret-down"),
    customFieldType(FieldDbType.VARCHAR_255),
    custom(FieldDbType.VARCHAR_255)
    ;

    public boolean hasValue() {
        return hasValue;
    }
    public String getName() {
        return name();
    }

    public FieldDbType getDbType() {
        return dbType;
    }

    public String getFragmentName() {
        return fragmentName;
    }

    FieldType(FieldDbType dbType, String fragmentName) {
        this.hasValue = true;
        this.dbType = dbType;
        this.fragmentName = fragmentName;
    }

    FieldType(boolean hasValue, FieldDbType dbType) {
        this.hasValue = hasValue;
        this.dbType = dbType;
        this.fragmentName = null;
    }

    FieldType(boolean hasValue, String fragmentName) {
        this.hasValue = hasValue;
        this.dbType = null;
        this.fragmentName = fragmentName;
    }

    FieldType(FieldDbType dbType) {
        this.hasValue=true;
        this.dbType = dbType;
        this.fragmentName = null;
    }

    FieldType(boolean hasValue) {
        this.hasValue = hasValue;
        this.dbType = null;
        this.fragmentName = null;
    }

    FieldType(String fragmentName) {
        this.hasValue = true;
        this.dbType = null;
        this.fragmentName = fragmentName;
    }

    FieldType() {
        this.hasValue = true;
        this.dbType = null;
        this.fragmentName = null;
    }

    private boolean hasValue;
    private FieldDbType dbType;
    private String fragmentName;

}
