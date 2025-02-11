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

package com.openkoda.core.service.system;

import com.openkoda.controller.ComponentProvider;
import com.openkoda.core.customisation.FrontendMapping;
import com.openkoda.core.customisation.FrontendMappingMap;
import com.openkoda.core.form.FieldType;
import com.openkoda.core.form.FrontendMappingDefinition;
import com.openkoda.core.form.FrontendMappingFieldDefinition;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.openkoda.core.helper.NameHelper.toColumnName;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang.StringUtils.isNotBlank;

@Service
public class DatabaseValidationService extends ComponentProvider {

    @Autowired
    private DataSource dataSource;

    @Inject
    FrontendMappingMap frontendMappingMap;

    CamelCaseToUnderscoresNamingStrategy namingStrategy;

    @PostConstruct
    void init() {
        namingStrategy = new CamelCaseToUnderscoresNamingStrategy();
    }

    /**
     * Validate current database state against all registered {@link FrontendMappingFieldDefinition}
     * Builds the update .sql script content on the go
     *
     * @return update .sql script content
     */
    public String getUpdateScript(boolean includeOnlyMissingColumns) {
        debug("[validateDatabaseAndGetUpdateScript]");
        StringBuilder updateDatabaseScript = new StringBuilder();
        Map<String, String> tableNamesMap = repositories.unsecure.form.getNameAndTableNameAsMap();
        for(Map.Entry<String, FrontendMapping> frontendMappingEntry : frontendMappingMap.entrySet()) {
            String entityName = tableNamesMap.containsKey(frontendMappingEntry.getKey()) ? tableNamesMap.get(frontendMappingEntry.getKey()) : frontendMappingEntry.getKey();
            updateDatabaseScript.append(getUpdateScript(frontendMappingEntry.getValue().definition(), entityName, includeOnlyMissingColumns));
        }
        return updateDatabaseScript.toString();
    }
    public String getUpdateScript(FrontendMappingDefinition frontendMappingDefinition, String tableName, boolean includeOnlyMissingColumns){
        StringBuilder updateDatabaseScript = new StringBuilder();
        Set<FrontendMappingFieldDefinition> fields = Arrays.stream(frontendMappingDefinition.getDbTypeFieldsExplicitlyDefined())
                .collect(toSet());
        try {
            Map<String, String> tableColumns = getTableColumns(tableName);
            for(FrontendMappingFieldDefinition field : fields.stream().filter(f -> f.getType() != FieldType.files).toList()) {
                String columnName = toColumnName(field.getValueName());
                boolean addColumn = !includeOnlyMissingColumns || !tableColumns.containsKey(columnName);
                if(addColumn) {
                    String dbType = field.getType().getDbType().getValue();
                    updateDatabaseScript.append(String.format("ALTER TABLE %s ADD COLUMN IF NOT EXISTS %s %s;\r\n", tableName, columnName, dbType));
                    if(field.getType().equals(FieldType.organization_select)) {
                        updateDatabaseScript.append(String.format("alter table %s add constraint %s foreign key (%s) references %s(%s);\r\n",
                                tableName,
                                "fk_" + columnName,
                                columnName,
                                "organization",
                                "id"
                        ));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return updateDatabaseScript.toString();
    }

    /**
     * Validate provided columns names and types against current database state
     *
     * @param tableName
     * @param columns    Map<String, String> containing fieldName as key and its expected database type
     * @return Boolean is true when validation of column types does not find any inconsistencies
     */
    public Boolean validateColumnTypes(String tableName, Map<String, FrontendMappingFieldDefinition> columns, StringBuilder validationLog, StringBuilder updateDatabaseScript) {
        debug("[validateColumnTypes]");
        boolean validationSuccess = true;
        if(isNotBlank(tableName)) {
        try {
            Map<String, String> tableColumns = getTableColumns(tableName);
            if(tableColumns.isEmpty()) {
                validationLog.append(String.format("Table %s does not exist. Will be created on form import.\r\n", tableName));
            }
            for(Map.Entry<String, FrontendMappingFieldDefinition> column : columns.entrySet().stream().filter(e -> e.getValue().getType() != FieldType.files).collect(toSet())) {
                String columnName = toColumnName(column.getValue().getColumnName());
                if (!tableColumns.containsKey(columnName)) {
                    validationLog.append(String.format("Column %s not present in table %s\r\n", columnName, tableName));
//                  table does not contain column, add alter query
                        updateDatabaseScript.append(String.format("ALTER TABLE %s ADD COLUMN IF NOT EXISTS %s %s;\r\n",
                                tableName, columnName, column.getValue().getType().getDbType().getValue()));
                        FieldType columnFieldType = column.getValue().getType();
                        if (columnFieldType.equals(FieldType.organization_select)) {
                            updateDatabaseScript.append(
                                    String.format("alter table %s add constraint %s foreign key (%s) references %s(%s);\r\n",
                                            tableName,
                                            "fk_" + columnName,
                                            columnName,
                                            "organization",
                                            "id"
                                    ));
                        }
                    } else if (!column.getValue().getType().getDbType().getColumnType().equals(tableColumns.get(columnName))) {
                        validationSuccess = false;
                        validationLog.append(String.format("Table %s column %s type %s does not match db state (%s)\r\n",
                                tableName, columnName, column.getValue().getType().getDbType().getColumnType(), tableColumns.get(columnName)));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return validationSuccess;
    }

    private Map<String, String> getTableColumns(String tableName) throws SQLException {
        Connection connection = DataSourceUtils.getConnection(dataSource);
        DatabaseMetaData metaData = connection.getMetaData();
        Map<String, String> tableColumns = new HashMap<>();
        try(ResultSet columns = metaData.getColumns(null,null, tableName, null)){
            while(columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String typeName = columns.getString("TYPE_NAME");
                tableColumns.put(columnName, typeName);
//                String columnSize = columns.getString("COLUMN_SIZE");
//                String datatype = columns.getString("DATA_TYPE");
//                String isNullable = columns.getString("IS_NULLABLE");
//                String isAutoIncrement = columns.getString("IS_AUTOINCREMENT");
            }
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
        return tableColumns;
    }
}
