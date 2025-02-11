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

package com.openkoda.service.export.converter.impl;

import com.openkoda.controller.ComponentProvider;
import com.openkoda.model.component.ControllerEndpoint;
import com.openkoda.repository.ControllerEndpointRepository;
import com.openkoda.service.export.ClasspathComponentImportService.SyncStatus;
import com.openkoda.service.export.converter.YamlToEntityConverter;
import com.openkoda.service.export.dto.ControllerEndpointConversionDto;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

import static com.openkoda.service.export.FolderPathConstants.SUBDIR_ORGANIZATION_PREFIX;

@Component
public class ControllerEndpointYamlToEntityConverter extends ComponentProvider implements YamlToEntityConverter<ControllerEndpoint, ControllerEndpointConversionDto> {
    private final ControllerEndpointRepository controllerEndpointRepository;
    @Value("${components.export.syncWithFilesystem:false}")
    private boolean syncWithFilesystem;

    public ControllerEndpointYamlToEntityConverter(ControllerEndpointRepository controllerEndpointRepository) {
        super();
        this.controllerEndpointRepository = controllerEndpointRepository;
    }

    @Override
    public ControllerEndpoint convertAndSave(ControllerEndpointConversionDto dto, String filePath) {
        debug("[convertAndSave]");

        ControllerEndpoint controllerEndpoint = getControllerEndpoint(dto);
        controllerEndpoint.setCode(loadResourceAsString(dto.getCode()));
        return repositories.unsecure.controllerEndpoint.save(controllerEndpoint);

    }
    @Override
    public ControllerEndpoint convertAndSave(ControllerEndpointConversionDto dto, String filePath, Map<String, String> resources) {
        debug("[convertAndSave]");

        ControllerEndpoint controllerEndpoint = getControllerEndpoint(dto);
        controllerEndpoint.setCode(resources.get(dto.getCode()));
        return repositories.unsecure.controllerEndpoint.save(controllerEndpoint);
    }

    private Long getOrgIdFromPath(String filePath) {
        String orgIdString = StringUtils.substringBetween(filePath, SUBDIR_ORGANIZATION_PREFIX, "/");
        return StringUtils.isNotEmpty(orgIdString) ? Long.parseLong(orgIdString) : null;
    }

    @NotNull
    private ControllerEndpoint getControllerEndpoint(ControllerEndpointConversionDto dto) {
        ControllerEndpoint controllerEndpoint = repositories.unsecure.controllerEndpoint.findByFrontendResourceIdAndSubPathAndHttpMethodAndOrganizationId(
                dto.getFrontendResourceId(), dto.getSubpath(), StringUtils.isNotEmpty(dto.getHttpMethod()) ? ControllerEndpoint.HttpMethod.valueOf(dto.getHttpMethod()) : null, dto.getOrganizationId());
        if(controllerEndpoint == null) {
            controllerEndpoint = new ControllerEndpoint();
            controllerEndpoint.setFrontendResourceId(dto.getFrontendResourceId());
            controllerEndpoint.setSubPath(dto.getSubpath() != null ? dto.getSubpath() : "");
            controllerEndpoint.setHttpMethod(dto.getHttpMethod());
            controllerEndpoint.setOrganizationId(dto.getOrganizationId());
        }
        controllerEndpoint.setHttpHeaders(dto.getHttpHeaders());
        controllerEndpoint.setModelAttributes(dto.getModelAttributes());
        controllerEndpoint.setResponseType(dto.getResponseType());
        controllerEndpoint.setModuleName(dto.getModule());
        return controllerEndpoint;
    }

    @Override
    public boolean hasContent() {
        return true;
    }

    @Override
    public SyncStatus checkSyncStatus(ControllerEndpointConversionDto dto) {
        Optional<ControllerEndpoint> a = controllerEndpointRepository.findOne(
                Example.of(new ControllerEndpoint(
                        dto.getOrganizationId(),
                        dto.getModule(),
                        dto.getSubpath(),
                        ControllerEndpoint.HttpMethod.valueOf(dto.getHttpMethod()))));
        if (a.isEmpty()) {
            return SyncStatus.NEW;
        }
        ControllerEndpoint ce = a.get();

        boolean same = true;
        same &= StringUtils.equals(String.valueOf(ce.getResponseType()), String.valueOf(dto.getResponseType()));
        same &= StringUtils.equals(String.valueOf(ce.getHttpHeaders()),  String.valueOf(dto.getHttpHeaders()));
        same &= StringUtils.equals(String.valueOf(ce.getModelAttributes()), String.valueOf(dto.getModelAttributes()));
        if (same) {
            String code = loadResourceAsString(dto.getCode());
            same = StringUtils.equals(code, ce.getCode());
        }
        return same ? SyncStatus.UNCHANGED : SyncStatus.MODIFIED;
    }

}
