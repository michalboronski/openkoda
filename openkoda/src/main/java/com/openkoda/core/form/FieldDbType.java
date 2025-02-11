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

public enum FieldDbType {

    VARCHAR_255("varchar(255)", "varchar"),
    VARCHAR_1000("varchar(1000)", "varchar"),
    VARCHAR_262144("varchar(262144)", "varchar"),
    BIGINT("bigint", "int8"),
    NUMERIC("numeric", null),
    BOOLEAN("boolean", "bool"),
    DATE("date", null),
    TIMESTAMP_W_TZ("timestamp with time zone", "timestamptz"),
    TIME_W_TZ("time with time zone", "timetz"),
    ;

    private String value;
    private String columnType;

    FieldDbType(String value, String columnType) {
        this.value = value;
        this.columnType = columnType;
    }

    public String getColumnType() {
        return columnType != null ? columnType : value;
    }

    public String getValue() {
        return value;
    }
}
