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
import com.openkoda.model.component.Scheduler;
import com.openkoda.model.component.event.EventListenerEntry;
import com.openkoda.service.export.ClasspathComponentImportService;
import com.openkoda.service.export.converter.YamlToEntityConverter;
import com.openkoda.service.export.converter.YamlToEntityParentConverter;
import com.openkoda.service.export.dto.EventListenerEntryConversionDto;
import com.openkoda.service.export.dto.SchedulerConversionDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

import static com.openkoda.service.export.ClasspathComponentImportService.SyncStatus.*;

@Component
@YamlToEntityParentConverter(dtoClass = SchedulerConversionDto.class)
public class SchedulerYamlToEntityConverter extends ComponentProvider implements YamlToEntityConverter<Scheduler, SchedulerConversionDto> {

    @Override
    public Scheduler convertAndSave(SchedulerConversionDto dto, String filePath) {
        debug("[convertAndSave]");

        Scheduler scheduler = new Scheduler();
        scheduler.setCronExpression(dto.getCronExpression());
        scheduler.setEventData(dto.getEventData());
        scheduler.setOnMasterOnly(dto.isOnMasterOnly());
        scheduler.setModuleName(dto.getModule());
        scheduler.setOrganizationId(dto.getOrganizationId());

        return repositories.secure.scheduler.saveOne(scheduler);
    }

    @Override
    public Scheduler convertAndSave(SchedulerConversionDto dto, String filePath, Map<String, String> resources) {
        Scheduler scheduler = convertAndSave(dto, filePath);
        services.scheduler.schedule(scheduler);
        return scheduler;
    }

    @Override
    public ClasspathComponentImportService.SyncStatus checkSyncStatus(SchedulerConversionDto dto) {
        Scheduler s = repositories.unsecure.scheduler.findByOrganizationIdAndEventDataAndCronExpression(
                dto.getOrganizationId(),
                        dto.getEventData(),
                        dto.getCronExpression());
        if (s == null) {
            return NEW;
        }

        boolean same = s.isOnMasterOnly() == dto.isOnMasterOnly();
        return same ? UNCHANGED : MODIFIED;
    }

}
