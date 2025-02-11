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
import com.openkoda.model.component.event.EventListenerEntry;
import com.openkoda.service.export.ClasspathComponentImportService;
import com.openkoda.service.export.converter.YamlToEntityConverter;
import com.openkoda.service.export.converter.YamlToEntityParentConverter;
import com.openkoda.service.export.dto.EventListenerEntryConversionDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

import static com.openkoda.service.export.ClasspathComponentImportService.SyncStatus.*;

@Component
@YamlToEntityParentConverter(dtoClass = EventListenerEntryConversionDto.class)
public class EventListenerEntryYamlToEntityConverter extends ComponentProvider implements YamlToEntityConverter<EventListenerEntry, EventListenerEntryConversionDto> {

    @Override
    public EventListenerEntry convertAndSave(EventListenerEntryConversionDto dto, String filePath) {
        debug("[convertAndSave]");

        EventListenerEntry eventListenerEntry = new EventListenerEntry();
        eventListenerEntry.setConsumerClassName(dto.getConsumerClassName());
        eventListenerEntry.setConsumerMethodName(dto.getConsumerMethodName());
        eventListenerEntry.setConsumerParameterClassName(dto.getConsumerParameterClassName());
        eventListenerEntry.setEventClassName(dto.getEventClassName());
        eventListenerEntry.setEventName(dto.getEventName());
        eventListenerEntry.setEventObjectType(dto.getEventObjectType());
        eventListenerEntry.setIndexString(dto.getIndexString());
        eventListenerEntry.setStaticData1(dto.getStaticData1());
        eventListenerEntry.setStaticData2(dto.getStaticData2());
        eventListenerEntry.setStaticData3(dto.getStaticData3());
        eventListenerEntry.setStaticData4(dto.getStaticData4());
        eventListenerEntry.setModuleName(dto.getModule());
        eventListenerEntry.setOrganizationId(dto.getOrganizationId());
        return repositories.secure.eventListener.saveOne(eventListenerEntry);
    }

    @Override
    public EventListenerEntry convertAndSave(EventListenerEntryConversionDto dto, String filePath, Map<String, String> resources) {
        debug("[convertAndSave]");
        EventListenerEntry eventListenerEntry = convertAndSave(dto, filePath);
        services.eventListener.registerListenerClusterAware(eventListenerEntry);
        return eventListenerEntry;
    }

    @Override
    public ClasspathComponentImportService.SyncStatus checkSyncStatus(EventListenerEntryConversionDto dto) {
        Optional<EventListenerEntry> a = repositories.unsecure.eventListener.findOne(
                Example.of(new EventListenerEntry(dto.getOrganizationId(), dto.getModule(),
                            dto.getEventClassName(),
                            dto.getEventName(),
                            dto.getEventObjectType(),
                            dto.getConsumerClassName(),
                            dto.getConsumerMethodName())));
        if (a.isEmpty()) {
            return NEW;
        }
        EventListenerEntry el = a.get();

        boolean same = true;
        same &= StringUtils.equals(String.valueOf(el.getStaticData1()), String.valueOf(dto.getStaticData1()));
        same &= StringUtils.equals(String.valueOf(el.getStaticData2()), String.valueOf(dto.getStaticData2()));
        same &= StringUtils.equals(String.valueOf(el.getStaticData3()), String.valueOf(dto.getStaticData3()));
        same &= StringUtils.equals(String.valueOf(el.getStaticData4()), String.valueOf(dto.getStaticData4()));

        return same ? UNCHANGED : MODIFIED;
    }

}
