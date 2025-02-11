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

package com.openkoda.dto;

public class DataAccessDto implements OrganizationRelatedObject{

    public Long id;
    public String name;
    public String code;
    public boolean advanced;
    public Long organizationId;
    public String readPrivilege;
    public String writePrivilege;
    public boolean registerAsAuditable;
    public boolean registerEntityEvent = true;
    public boolean registerApiCrudController;
    public boolean registerHtmlCrudController;
    public boolean showOnOrganizationDashboard = true;
    public String tableColumns;
    public String filterColumns;
    public String existingTableName;
    public String newTableName;
    public boolean createNewTable;
    public String columnNames;
    public String tableView;

    public Long getId() {
        return id;
    }

    public Long setId(Long id) {
        this.id = id;
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public boolean isRegisterAsAuditable() {
        return registerAsAuditable;
    }

    public void setRegisterAsAuditable(boolean registerAsAuditable) {
        this.registerAsAuditable = registerAsAuditable;
    }

    public boolean isRegisterApiCrudController() {
        return registerApiCrudController;
    }

    public void setRegisterApiCrudController(boolean registerApiCrudController) {
        this.registerApiCrudController = registerApiCrudController;
    }

    public boolean isRegisterHtmlCrudController() {
        return registerHtmlCrudController;
    }

    public void setRegisterHtmlCrudController(boolean registerHtmlCrudController) {
        this.registerHtmlCrudController = registerHtmlCrudController;
    }

    public boolean isShowOnOrganizationDashboard() {
        return showOnOrganizationDashboard;
    }

    public void setShowOnOrganizationDashboard(boolean showOnOrganizationDashboard) {
        this.showOnOrganizationDashboard = showOnOrganizationDashboard;
    }

    public String getTableColumns() {
        return tableColumns;
    }

    public void setTableColumns(String tableColumns) {
        this.tableColumns = tableColumns;
    }

    public String getFilterColumns() {
        return filterColumns;
    }

    public void setFilterColumns(String filterColumns) {
        this.filterColumns = filterColumns;
    }

    public String getExistingTableName() {
        return existingTableName;
    }

    public void setExistingTableName(String existingTableName) {
        this.existingTableName = existingTableName;
    }

    public String getNewTableName() {
        return newTableName;
    }

    public void setNewTableName(String newTableName) {
        this.newTableName = newTableName;
    }

    public boolean isCreateNewTable() {
        return createNewTable;
    }

    public void setCreateNewTable(boolean createNewTable) {
        this.createNewTable = createNewTable;
    }

    public String getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String columnNames) {
        this.columnNames = columnNames;
    }

    public String getTableName(){
        if(isCreateNewTable()){
            return getNewTableName();
        }
        return getExistingTableName();
    }

    public String getTableView() {
        return tableView;
    }

    public void setTableView(String tableView) {
        this.tableView = tableView;
    }

    public String getReadPrivilege() {
        return readPrivilege;
    }

    public void setReadPrivilege(String readPrivilege) {
        this.readPrivilege = readPrivilege;
    }

    public String getWritePrivilege() {
        return writePrivilege;
    }

    public void setWritePrivilege(String writePrivilege) {
        this.writePrivilege = writePrivilege;
    }

    public boolean isAdvanced() {
        return advanced;
    }

    public void setAdvanced(boolean advanced) {
        this.advanced = advanced;
    }
    
    public void setRegisterEntityEvent(boolean registerEntityEvent) {
        this.registerEntityEvent = registerEntityEvent;
    }
    
    public boolean isRegisterEntityEvent() {
        return registerEntityEvent;
    }
}
