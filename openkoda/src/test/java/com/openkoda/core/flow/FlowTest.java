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

package com.openkoda.core.flow;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static com.openkoda.core.flow.TestPageAttributes.someMessageString;


/**
 * Created by arek on 2016-01-18.
 */
public class FlowTest {

   Flow testFlow = Flow.init().then(a -> Arrays.asList(1, 2, 3, 4, 5))
           .then(a -> a.result.stream().map( i -> i + 2).reduce(0, Integer::sum))
           .thenSet(someMessageString, a -> "The result is " + a.result)
           .thenSet("otherMessageString", a -> "Agreed. " + a.result);

   @Test
   public void testFlowExecute() {
       PageModelMap result = testFlow.execute();
       Assert.assertEquals("The result is 25", result.get(someMessageString));
   }

   @Test
   public void testStringFlowExecute() {


       PageModelMap result = testFlow.execute();
       Assert.assertEquals("The result is 25", result.get(someMessageString));
       Assert.assertEquals("Agreed. The result is 25", result.get("otherMessageString"));
   }

}
