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

package com.openkoda.uicomponent;


import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;


@Component
public class JsParser {

    public List<String> getFunctions(String code){
        List<String> functions = new ArrayList<>();
        for(JsPattern jsPattern : JsPattern.values()){
            addFunctions(jsPattern, code, functions);
        }
        return functions;
    }

    private  void addFunctions(JsPattern jsPattern, String code, List<String> functions) {
        Matcher matcher = jsPattern.getPattern().matcher(code);
        while(matcher.find()){
            functions.add(matcher.group(jsPattern.getNameGroup()).trim() + "(" + matcher.group(jsPattern.getArgsGroup()).trim().replaceAll(" ","") + ")");
        }
    }

    private enum JsPattern {
        function_standard(compile("export\\s+function\\s+([^)]*)\\s*\\(([^)]*)"), 1 ,2),
        function_equals(compile("export\\s+(const|var|let)\\s+([^=]*)\\s*=\\s*function\\s*\\(([^)]*)\\)"), 2 ,3),
        function_lambda(compile("export\\s+(const|var|let)\\s+([^=]*)\\s*=\\s*\\(*([^)]*)\\)*\\s*=>"), 2 ,3);

        final Pattern pattern;
        final Integer nameGroup;
        final Integer argsGroup;

        JsPattern(Pattern pattern, Integer nameGroup, Integer argsGroup) {
            this.pattern = pattern;
            this.nameGroup = nameGroup;
            this.argsGroup = argsGroup;
        }

        Pattern getPattern() {
            return pattern;
        }

        Integer getNameGroup() {
            return nameGroup;
        }

        Integer getArgsGroup() {
            return argsGroup;
        }
    }
}
