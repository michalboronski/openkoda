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

import com.openkoda.core.form.FrontendMappingFieldDefinition;

import java.util.*;
import java.util.stream.Collectors;

import static com.openkoda.core.helper.NameHelper.*;

public class DynamicEntityDescriptorFactory {

    private static final Map<String /* formName */, DynamicEntityDescriptor> map = new HashMap<>();

    public static List<DynamicEntityDescriptor> loadableInstances(){
        return map.values().stream().filter(DynamicEntityDescriptor::isLoadable).collect(Collectors.toList());
    }

    public static List<DynamicEntityDescriptor> instances(){
        return new ArrayList<>(map.values());
    }

    public static void create(String formName, String tableName, Collection<FrontendMappingFieldDefinition> fields, Long timeMillis){
        DynamicEntityDescriptor ded = new DynamicEntityDescriptor(toEntityClassName(formName), tableName, toEntityKey(formName), toRepositoryName(formName), fields, timeMillis);
        map.put(ded.getEntityKey(), ded);
    }


    public static DynamicEntityDescriptor getInstanceByEntityKey(String entityKey) {
        return map.get(entityKey);
    }
}
