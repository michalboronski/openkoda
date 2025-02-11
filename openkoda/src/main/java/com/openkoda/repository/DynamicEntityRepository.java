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

import com.openkoda.core.repository.common.UnsecuredFunctionalRepositoryWithLongId;
import com.openkoda.model.DynamicEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public interface DynamicEntityRepository extends UnsecuredFunctionalRepositoryWithLongId<DynamicEntity>{

    @Query(value="select de.tableName from DynamicEntity de")
    List<Object> getTableNames();
    @Query(nativeQuery = true, value = """
            select table_name, string_agg(' ' || column_name, ',') from
               (SELECT table_name, column_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name IN
               (select distinct tablename from pg_tables
                inner join dynamic_entity as de on de.table_name=pg_tables.tablename and pg_tables.schemaname='public')) x
            group by table_name
            """)
    List<Object[]> getDynamicTablesColumnNamesQuery();

    default Map<String,String> getDynamicTablesColumnNames(){
        return getDynamicTablesColumnNamesQuery().stream().collect(Collectors.toMap(l -> (String) ((Object[])l)[0], l ->  (String)((Object[])l)[1]));
    }

    boolean existsByTableName(String tableName);
}
