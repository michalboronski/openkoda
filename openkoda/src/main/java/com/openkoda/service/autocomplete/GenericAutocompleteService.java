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

package com.openkoda.service.autocomplete;

import com.openkoda.controller.ComponentProvider;
import com.openkoda.core.helper.ReflectionHelper;
import com.openkoda.uicomponent.annotation.Autocomplete;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

class GenericAutocompleteService extends ComponentProvider {
    @Autowired
    private ReflectionHelper helper;

    Map<String,String> getSuggestionsAndDocumentation(Method[] methods, String variableName){
        return stream(methods)
                .collect(toMap(m -> getSuggestion(variableName, m), this::getDocumentation, (m1, m2) -> !m1.equals("") ? m1 : m2 ));
    }
    String getSuggestion(String variableName, Method method){
        return (variableName != null ? variableName + "." : "") + helper.getNameWithParamNames(method);
    }
    String getDocumentation(Method method){
        return method.getAnnotation(Autocomplete.class).doc();
    }
    Method[] getExposedMethods(String className){
        return stream(helper.getDeclaredMethods(className))
                .filter(f -> f.isAnnotationPresent(Autocomplete.class))
                .toArray(Method[]::new);
    }
}
