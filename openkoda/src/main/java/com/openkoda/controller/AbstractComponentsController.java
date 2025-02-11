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

package com.openkoda.controller;

import com.openkoda.core.flow.Flow;
import com.openkoda.service.export.ClasspathComponentImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

public class AbstractComponentsController extends ComponentProvider {

    public AbstractComponentsController() {
        // TODO Auto-generated constructor stub
    }

    @Autowired
    private ClasspathComponentImportService classpathComponentImportService;

    public Object importComponentsZip(MultipartFile file, Boolean delete) {
        debug("[importComponentsZip]");
        return Flow.init()
                .thenSet(importLog ,a -> services.zipComponentImport.loadResourcesFromZip(file, delete))
                .execute()
                .mav("components");
    }

    public Object reloadFromResources(String componentPath) {
        debug("[reloadFromResources]");
        return Flow.init()
                .then(a -> classpathComponentImportService.loadYamlFile(componentPath))
                .execute()
                .mav(s -> true, f -> false);
    }
}
