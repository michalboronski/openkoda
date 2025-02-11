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

package com.openkoda.core.configuration;

import com.openkoda.controller.common.URLConstants;
import com.openkoda.model.component.FrontendResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.openkoda.controller.common.URLConstants.FRONTENDRESOURCEREGEX;
import static com.openkoda.service.export.FolderPathConstants.FRONTEND_RESOURCE_;

@Component
public class TemplatePathFilteringProcessor {

    private static final Pattern accessLevelPath = Pattern.compile(FRONTEND_RESOURCE_
            + "(" + Arrays.stream(FrontendResource.AccessLevel.values()).map(al -> al.toString().toLowerCase()).collect(Collectors.joining("|")) + ")/"
            + FRONTENDRESOURCEREGEX + "$");
    
    public static class FilteredTemplatePath {
        private boolean email = false;
        private FrontendResource.AccessLevel accessLevel;

        private String filteredTemplate;
        private String filteredResourceName;
        private String frontendResourceEntryName;

        public boolean isEmail() {
            return email;
        }

        public void setEmail(boolean isEmail) {
            this.email = isEmail;
        }

        public FrontendResource.AccessLevel getAccessLevel() {
            return accessLevel;
        }

        public void setAccessLevel(FrontendResource.AccessLevel accessLevel) {
            this.accessLevel = accessLevel;
        }

        public String getFilteredTemplate() {
            return filteredTemplate;
        }

        public void setFilteredTemplate(String filteredTemplate) {
            this.filteredTemplate = filteredTemplate;
        }

        public String getFilteredResourceName() {
            return filteredResourceName;
        }

        public void setFilteredResourceName(String filteredResourceName) {
            this.filteredResourceName = filteredResourceName;
        }

        public String getFrontendResourceEntryName() {
            return frontendResourceEntryName;
        }

        public void setFrontendResourceEntryName(String frontendResourceEntryName) {
            this.frontendResourceEntryName = frontendResourceEntryName;
        }
    }

    public FilteredTemplatePath processTemplatePath(String template, String resourceName,
            FrontendResource.AccessLevel tenantedResourceAccessLevel) {

        FilteredTemplatePath filteredPath = new FilteredTemplatePath();
        filteredPath.setAccessLevel(tenantedResourceAccessLevel);
        filteredPath.setFilteredTemplate(template);
        filteredPath.setFilteredResourceName(resourceName);

        // resolve template access level
        Matcher m = accessLevelPath.matcher(template);
        if (m.matches()) {
            filteredPath.setAccessLevel(FrontendResource.AccessLevel.valueOf(m.group(1).toUpperCase()));
        }

        int emailPath = -1;
        if (template.endsWith(URLConstants.EMAILRESOURCE_DISCRIMINATOR)) {
            emailPath = template.indexOf("email");
            filteredPath.setEmail(true);
            filteredPath.setFilteredTemplate(template.substring(0, template.length() - 1));
            filteredPath.setFilteredResourceName(resourceName.replace(URLConstants.EMAILRESOURCE_DISCRIMINATOR, ""));
            filteredPath
                    .setFrontendResourceEntryName(StringUtils.substring(template, emailPath, template.length() - 1));
        } else {
            filteredPath.setFrontendResourceEntryName(StringUtils.substringAfterLast(template, "/"));
        }

        return filteredPath;
    }
}
