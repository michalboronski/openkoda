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

package com.openkoda.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.sql.internal.NativeQueryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;

import static com.openkoda.controller.common.URLConstants.LOWERCASE_NUMERIC_UNDERSCORE_REGEXP;
import static com.openkoda.core.security.OrganizationUser.nonExistingOrganizationId;

@Repository
public class NativeQueries {
    @Autowired
    EntityManager entityManager;

    @Autowired
    private DataSource dataSource;

    public boolean createTable(String tableName){
        if(tableName.matches(LOWERCASE_NUMERIC_UNDERSCORE_REGEXP)) {
            entityManager.createNativeQuery(createTableSql(tableName)).executeUpdate();
            return true;
        }
        return false;
    }

    public boolean dropTable(String tableName){
        if(tableName.matches(LOWERCASE_NUMERIC_UNDERSCORE_REGEXP)) {
            entityManager.createNativeQuery(dropTableSql(tableName)).executeUpdate();
            return true;
        }
        return false;
    }

    public String createTableSql(String tableName) {
//        long orgId = tr.organizationId == null ?
        return String.format(
                """
                    CREATE TABLE %s
                    (id bigint NOT NULL,
                     created_by character varying(255),
                     created_by_id bigint,
                     created_on timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
                     index_string character varying(16300)  DEFAULT '',
                     modified_by character varying(255),
                     modified_by_id bigint, organization_id bigint,
                     updated_on timestamp with time zone DEFAULT CURRENT_TIMESTAMP);
                     ALTER TABLE %s ENABLE ROW LEVEL SECURITY;
                     CREATE POLICY %s_org on %s FOR ALL USING
                     (current_setting('openkoda.org_id',true) IS NULL OR %d = (current_setting('openkoda.org_id',true)\\:\\:bigint) OR organization_id = (current_setting('openkoda.org_id',true)\\:\\:bigint));
                """,
            tableName, tableName, tableName, tableName, nonExistingOrganizationId);
    }

    public String dropTableSql(String tableName) {
        return String.format(
                """
                    DROP TABLE %s CASCADE;
                """,
                tableName);
    }

    public void runUpdateQuery(String sqlUpdateScript) throws SQLException {
        Connection connection = dataSource.getConnection();
        ScriptUtils.executeSqlScript(connection, new ByteArrayResource(sqlUpdateScript.getBytes()));
    }
    public boolean ifTableExists(String tableName){
           return (Boolean) entityManager.createNativeQuery(tableExistsSql()).setParameter("tableName", tableName)
                   .getSingleResult();
    }

    public String tableExistsSql() {
        return """
                SELECT EXISTS (
                    SELECT FROM
                        pg_tables
                    WHERE
                        tablename  = :tableName)""";
    }

    @Transactional(readOnly = true)
    public List<LinkedHashMap<String, Object>> runReadOnly(String query) {
        Query q1 = entityManager.createNativeQuery(StringUtils.substringBefore(query,";"));
        NativeQueryImpl nativeQuery = (NativeQueryImpl) q1;
        nativeQuery.setResultTransformer(AliasToEntityHashMapResultTransformer.INSTANCE);
        return nativeQuery.getResultList();
    }
}
