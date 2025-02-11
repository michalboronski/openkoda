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

package com.openkoda.controller.report;

import com.openkoda.core.controller.frontendresource.AbstractFrontendResourceController;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.openkoda.controller.common.URLConstants.*;


@Controller
@RequestMapping({_HTML + _QUERY_REPORT, _HTML_ORGANIZATION_ORGANIZATIONID + _QUERY_REPORT})
@Profile("!development")
public class BasicAIReportController extends AbstractFrontendResourceController {

    public Object sendPrompt(@PathVariable(name=ORGANIZATIONID, required = false) Long organizationId,
                             @RequestParam("prompt") String prompt,
                             @RequestParam("promptWebEndpoint") String promptWebEndpoint,
                             @RequestParam("conversationId") String conversationId,
                             @RequestParam(value = "channelId", required = false, defaultValue = "") String channelId,
                             @RequestParam(value = "model", required = false, defaultValue = "gpt-4-0613") String model,
                             @RequestParam(value = "temperature", required = false, defaultValue = "0.2") String temperature) {
        debug("[sendPrompt]");
        throw new NotImplementedException();
    }

}
