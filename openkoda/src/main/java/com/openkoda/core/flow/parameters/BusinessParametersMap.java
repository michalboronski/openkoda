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

package com.openkoda.core.flow.parameters;

import com.openkoda.core.flow.ParametersService;
import com.openkoda.model.business.BusinessParameter;
import com.openkoda.repository.business.BusinessParameterRepository;
import com.openkoda.uicomponent.UtilServices;
import jakarta.inject.Inject;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;

@Service
public class BusinessParametersMap extends HashMap<String, Object> implements ParametersService {

    @Inject
    BusinessParameterRepository businessParameterRepository;

    @Inject
    UtilServices utilServices;

    @Override
    public String getAsText(String propertyName) {
        return getAsText(null, propertyName);
    }

    @Override
    public long getAsLong(String propertyName) {
        return getAsLong(null, propertyName);
    }

    @Override
    public BigDecimal getAsDecimal(String propertyName) {
        return getAsDecimal(null, propertyName);
    }

    public String getAsText(Long organizationId, String propertyName) {
        BusinessParameter businessParameter = businessParameterRepository.findByNameAndOrganizationId(propertyName, organizationId);
        return businessParameter != null ? businessParameter.getValue() : null;
    }

    public long getAsLong(Long organizationId, String propertyName) {
        BusinessParameter businessParameter = businessParameterRepository.findByNameAndOrganizationId(propertyName, organizationId);
        return businessParameter != null ? utilServices.parseLong(businessParameter.getValue()) : -1L;
    }

    public BigDecimal getAsDecimal(Long organizationId, String propertyName) {
        BusinessParameter businessParameter = businessParameterRepository.findByNameAndOrganizationId(propertyName, organizationId);
        return businessParameter != null ? utilServices.parseDecimal(businessParameter.getValue()) : null;
    }
}
