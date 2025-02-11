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

package com.openkoda.service.dynamicentity;

import com.openkoda.controller.ComponentProvider;
import com.openkoda.model.DynamicEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Service
public class DynamicEntityService extends ComponentProvider {

    @Transactional(propagation = REQUIRES_NEW)
    public boolean createDynamicTableIfNotExists(String tableName){
        debug("[createDynamicTableIfNotExists] {}", tableName);
        if(!repositories.unsecure.nativeQueries.ifTableExists(tableName)) {
            repositories.unsecure.nativeQueries.createTable(tableName);
            repositories.unsecure.dynamicEntity.save(create(tableName));
            return true;
        }
        return false;
    }

    public boolean createDynamicEntityIfNotExists(String tableName){
        debug("[createDynamicEntityIfNotExists] {}", tableName);
        if(!repositories.unsecure.dynamicEntity.existsByTableName(tableName)) {
            repositories.unsecure.dynamicEntity.save(create(tableName));
            return true;
        }
        return false;
    }
    
    public Map<Object, String> getAll() {
        Map<Object, String> eventsClasses = new LinkedHashMap<>();
        eventsClasses.put(String.class.getName(), String.format("Plain String (%s)", String.class.getName()));
        services.dynamicEntityRegistration.dynamicEntityClasses.entrySet() .stream().forEach( de -> {
            eventsClasses.put(de.getValue().getName(), String.format(" %s (%s)", de.getKey(), de.getValue().getName()));
        });
        
        return eventsClasses;
    }

    private DynamicEntity create(String tableName){
        DynamicEntity dynamicEntity = new DynamicEntity();
        dynamicEntity.setTableName(tableName);
        return dynamicEntity;
    }
}
