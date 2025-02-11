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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The values served to the tables of the generic views are represented as list of lists (or in other words as list of rows)
 * in the template model.<br>
 * This class is meant to be representation for the rows. Apart from being iterable it can hold additional data - references
 * identifiers, currently need to build reference links. The "regular" table data is available during iteration,
 * but the "additional" data must be requested via getters.
 */
public class ReferenceIdAwareList extends ArrayList<Object> {
    private final Map<String, Object> referenceIds = new HashMap<>();
    public Object getReferenceIdOf(String referenceName) {
        return referenceIds.getOrDefault(referenceName, null);
    }
    public void putReferenceIdOf(String referenceName, Object referenceId) {
        referenceIds.put(referenceName, referenceId);
    }
}
