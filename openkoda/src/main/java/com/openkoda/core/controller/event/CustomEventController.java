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

package com.openkoda.core.controller.event;


import com.openkoda.core.controller.generic.AbstractController;
import com.openkoda.core.flow.Flow;
import com.openkoda.core.security.HasSecurityRules;
import com.openkoda.core.service.event.CustomApplicationEvent;
import com.openkoda.core.service.event.EntityApplicationEvent;
import com.openkoda.form.CreateEventForm;
import com.openkoda.model.component.event.EventListenerEntry;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.openkoda.controller.common.URLConstants._CUSTOM_EVENT;
import static com.openkoda.controller.common.URLConstants._HTML;

/**
 * Handles basic operations related to Custom Events management
 * 
 */
@RestController
@RequestMapping(_HTML + _CUSTOM_EVENT)
public class CustomEventController extends AbstractController implements HasSecurityRules {

    /**
     * Prepares model and view to display all {@link EventListenerEntry} page
     * See also {@link AbstractEventListenerController}
     *
     * @param pageable
     * @param search
     * @return java.lang.Object
     */
    @PreAuthorize(CHECK_CAN_READ_BACKEND)
    @GetMapping(value = _ALL)
    public Object getAll(
            @Qualifier("event") Pageable pageable,
            @RequestParam(required = false, defaultValue = "", name = "event_search") String search) {
        debug("[getAll]");
        return Flow.init()
                .thenSet(customEventPage, a -> repositories.secure.customEvent.search(search, null, null, pageable))
                .then( a -> { 
                            a.result.get()
                                .filter( EntityApplicationEvent.class::isInstance)
                                .map( e -> (EntityApplicationEvent)e)
                                .forEach( event -> {
                                    EntityApplicationEvent custom = (EntityApplicationEvent)services.customEventService.getEventAndEntityMap().get(event.getEventName());
                                    event.setEventClass(event.getEventClass());
                                    // it might be event's class is not yet created
                                    if(custom != null) {
                                        event.setClassName(custom.getClassName());
                                        event.setEventName(custom.getEventName());
                                        event.setEventClass(custom.getEventClass());
                                    }
                                    
                                debug("[getAll]", event.toAuditString());
                            });
                            return a.result;
                })
                .execute()
                .mav("customevent-" + ALL);
    }
    
    @PreAuthorize(CHECK_CAN_READ_BACKEND)
    @GetMapping(value = _NEW_SETTINGS)
    public Object createEvent() {
        return Flow.init()
                .thenSet(createEventForm, a -> new CreateEventForm<String>(new CustomApplicationEvent<String>(String.class, null)))
                .execute()
                .mav("customevent-settings");
    }
    
    /**
     * Saves new {@link com.openkoda.core.} in the database
     */
    @PreAuthorize(CHECK_CAN_MANAGE_BACKEND)
    @PostMapping(_NEW_SETTINGS)
    public Object saveNew(@Valid CreateEventForm<String> eventForm, BindingResult br) {
        debug("[saveNew]");
        return Flow.init(createEventForm, eventForm)
                //.then(a -> services.privilege.checkIfPrivilegeNameAlreadyExists(eventForm.dto.getName(), br))
                .then(a -> services.validation.validate(eventForm, br))
                .then(a -> services.customEventService.createCustomEvent(a.result.getDto()))
                .then(a -> { services.customEventService.registerCustomEvent(a.result); return a.result; })
                .thenSet(createEventForm, a -> eventForm)
                .execute()
                .mav(ENTITY + '-' + FORMS + "::customevent-settings-form-success",
                        ENTITY + '-' + FORMS + "::customevent-settings-form-error");
    }
    
    /**
     * Saves new {@link com.openkoda.core.} in the database
     */
    @PreAuthorize(CHECK_CAN_MANAGE_BACKEND)
    @PostMapping(value = _ID_REMOVE)
    public Object remove(@PathVariable(ID) Long eventId) {
        debug("[remove]");
        return Flow.init(eventId)
                .thenSet(eventToUnregister, a -> repositories.unsecure.customEvent.findOne(eventId))
                .then(a -> services.customEventService.unregisterDynamicEntityEvent(a.result))
                //.then(a -> repositories.unsecure.eventListener.deleteOne( a.result.getId() ) )
                //.then(a -> services.eventListener.unregisterEventListenerClusterAware(a.model.get(eventListenerEntityToUnregister)))
                .execute()
                .mav(ENTITY + '-' + FORMS + "::customevent-settings-form-success",
                        ENTITY + '-' + FORMS + "::customevent-settings-form-error");
    }
}
