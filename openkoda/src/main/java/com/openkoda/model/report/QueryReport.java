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

package com.openkoda.model.report;

import com.openkoda.model.PrivilegeNames;
import com.openkoda.model.common.OpenkodaEntity;
import com.openkoda.model.component.FrontendResource;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Formula;

@Entity
public class QueryReport extends OpenkodaEntity {

    private String name;

    @Column(length = 262144)

    private String query;

    @OneToOne
    @JoinColumn(name = "widget_id")
    private FrontendResource widget;

    @Formula("( '" + PrivilegeNames._readOrgData + "' )")
    private String requiredReadPrivilege;

    @Formula("( '" + PrivilegeNames._manageOrgData + "' )")
    private String requiredWritePrivilege;

    public QueryReport(Long organizationId) {
        super(organizationId);
    }

    public QueryReport() {
        super(null);
    }

    public QueryReport(String query) {
        super(null);
        this.query = query;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getFileName() {
        return StringUtils.isNotEmpty(name) ? name.replaceAll("\\s+", "_").toLowerCase() : "report";
    }

    public FrontendResource getWidget() {
        return widget;
    }

    public void setWidget(FrontendResource widget) {
        this.widget = widget;
    }

}
