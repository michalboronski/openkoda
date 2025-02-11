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

import org.hibernate.query.TypedTupleTransformer;
import org.hibernate.transform.ResultTransformer;

import java.util.LinkedHashMap;

public class AliasToEntityHashMapResultTransformer implements ResultTransformer<LinkedHashMap<String,Object>>, TypedTupleTransformer<LinkedHashMap<String,Object>> {

    public static final AliasToEntityHashMapResultTransformer INSTANCE = new AliasToEntityHashMapResultTransformer();

    /**
     * Disallow instantiation of AliasToEntityMapResultTransformer.
     */
    private AliasToEntityHashMapResultTransformer() {
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Class getTransformedType() {
        return LinkedHashMap.class;
    }

    @Override
    public LinkedHashMap<String,Object> transformTuple(Object[] tuple, String[] aliases) {
        LinkedHashMap<String,Object> result = new LinkedHashMap<>( tuple.length );
        for ( int i = 0; i < tuple.length; i++ ) {
            String alias = aliases[i];
            if ( alias != null ) {
                result.put( alias, tuple[i] );
            }
        }
        return result;
    }

    /**
     * Serialization hook for ensuring singleton uniqueing.
     *
     * @return The singleton instance : {@link #INSTANCE}
     */
    private Object readResolve() {
        return INSTANCE;
    }
}
