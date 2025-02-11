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

package com.openkoda.form;

import com.openkoda.core.form.AbstractEntityForm;
import com.openkoda.core.form.AbstractForm;
import com.openkoda.core.form.FrontendMappingDefinition;
import com.openkoda.core.service.event.CustomApplicationEvent;
import com.openkoda.dto.CanonicalObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class CreateEventForm<T> extends AbstractEntityForm<CustomApplicationEvent<T>, CustomApplicationEvent<T>> implements TemplateFormFieldNames {
    
    public CreateEventForm(FrontendMappingDefinition frontendMappingDefinition) {
        super(frontendMappingDefinition);
    }

    public CreateEventForm(CustomApplicationEvent<T> dto, FrontendMappingDefinition frontendMappingDefinition) {
        super(dto, null, frontendMappingDefinition);
    }

    public CreateEventForm(CustomApplicationEvent<T> dto, CustomApplicationEvent<T> entity, FrontendMappingDefinition frontendMappingDefinition) {
        super(dto, entity, frontendMappingDefinition);
    }

    public CreateEventForm(CustomApplicationEvent<T> entity) {
        super(new CustomApplicationEvent<T>(), entity, FrontendMappingDefinitions.createEventForm);
    }

    public CreateEventForm() {
        super(null, null, FrontendMappingDefinitions.createEventForm);
    }
    



    @Override
    protected CustomApplicationEvent<T> populateTo(CustomApplicationEvent<T> entity) {
        entity.setId(getSafeValue(entity.getId(), ID_));
        entity.setName(getSafeValue(entity.getName(), NAME_));
        entity.setEventName(getSafeValue(entity.getEventName(), "eventName"));
        entity.setClassName(getSafeValue(entity.getClassName(), "className"));
        entity.setEventData(getSafeValue(entity.getEventData(), "eventData"));
        return entity;
    }

    @Override
    public CreateEventForm<T> validate(BindingResult br) {
        if (isBlank(dto.getName())) {
            br.rejectValue("dto.name", "not.empty");
        }

        return this;
    }

    @Override
    protected <F extends AbstractEntityForm<CustomApplicationEvent<T>, CustomApplicationEvent<T>>> F populateFrom(
            CustomApplicationEvent<T> entity) {
        dto.setName(entity.getName());
        dto.setEventName(entity.getEventName());
        dto.setClassName(entity.getClassName());
        dto.setEventClass(entity.getEventClass());
        dto.setEventData(entity.getEventData());
        return (F) this;
    }
}
