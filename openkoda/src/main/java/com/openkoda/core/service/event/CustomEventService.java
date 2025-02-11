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

package com.openkoda.core.service.event;

import com.openkoda.controller.ComponentProvider;
import com.openkoda.core.tracker.LoggingComponentWithRequestId;
import com.openkoda.dto.DataAccessDto;
import com.openkoda.model.common.OpenkodaEntity;
import jakarta.inject.Inject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

/**
 * Provides basic CRUD operations for CustomApplicationEvents. It also implements logic for registration custom events in a listenter service.  
 *
 * @author mboronski
 * @since 27-06-2024
 */
@Service
public class CustomEventService extends ComponentProvider implements LoggingComponentWithRequestId {

    /**
     * used for events publishing
     */
    @Inject private ApplicationEventService eventService;
    
    /**
     * A map that helps finding matching CustomApplicationEvent template object based on a entity-key 
     */
    private Map<String, CustomApplicationEvent> eventAndEntityMap = new ConcurrentHashMap<>();
    
    /**
     * A map that helps finding matching CustomApplicationEvent template object based on target class type (for isntance some dynamic entity class type)
     */
    private Map<String, Class<? extends CustomApplicationEvent>> eventClassNameToClass = new ConcurrentHashMap<>();
    
    /**
     * For given key it finds apropropriate CustomApplicationEvent template object. 
     * @param <T> type of an object to be carried by the custom event
     * @param entityKey dynamic-entity key or a event name
     * @param clazz optional, used for generic type <T> 
     * @return
     */
    public <T> List<CustomApplicationEvent> findCustomEvents(String entityKey, Class<T> clazz) {
        // TODO : replace with a JPA query
        return repositories.unsecure.customEvent.findAll().stream().filter( ce -> ce.getName().equalsIgnoreCase(entityKey)).collect(Collectors.toList());
    }
    
    /**
     * finds custom events based on a class name
     * @param name
     * @return
     */
    public Class<? extends CustomApplicationEvent> findCustomEventClass(String name) {
        return this.eventClassNameToClass.get(name);
    }
    
    public Collection<Class<? extends CustomApplicationEvent>> getAllCustomEvents() {
        return eventClassNameToClass.values();
    }
    
    /**
     * For a given dto of Dynamic Entity this methods creates and persists categories of events, typical for DynamicENtioty, i.e. CREATED, MODIFIED, DELETED
     * @param dto
     * @return
     */
    @Transactional(propagation = REQUIRES_NEW)
    public List<EntityApplicationEvent> createDynamicEntityEvent(DataAccessDto dto) {
        debug("[createDynamicEntityEvent] for [{}]", dto.getName());
        List<EntityApplicationEvent> customEventForEntity = EntityApplicationEvent.COMMON_CATEGORIES.stream()
                .map( category -> new EntityApplicationEvent(null, dto.getName(), null, category)).toList();

        return repositories.unsecure.customEvent.saveAll(customEventForEntity);
    }
    
    /**
     * Creates a and persist a CustomApplicationEvent based on a prototyping/template event object
     * @param <T>
     * @param eventTemplate
     * @return
     */
    @Transactional(propagation = REQUIRES_NEW)
    public <T> CustomApplicationEvent<T> createCustomEvent(CustomApplicationEvent<T> eventTemplate) {
        debug("[createCustomEvent] for [{}]", eventTemplate);
        if(eventTemplate.getClassName() != null && eventTemplate.getEventClass() == null) {
            try {
                Class<?> clazz = Class.forName(eventTemplate.getClassName());
                if(OpenkodaEntity.class.isAssignableFrom(clazz)) {
                    eventTemplate = CustomApplicationEvent.newInstance(EntityApplicationEvent.class, clazz, eventTemplate.getName());
                } else {
                    eventTemplate.setEventClass((Class<T>) Class.forName(eventTemplate.getClassName()));
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        
        return repositories.unsecure.customEvent.save(eventTemplate);
    }
    
    @Transactional(propagation = REQUIRES_NEW)
    public Object updateDynamicEntityEvent(Object obj) {
        // TODO : could register ENtity event already at this point, but DynamicEntity is most probably not generated anyway - it would have to be done via ByteBuddy and loaded to classpath anyway
        // So for now there is nothing to do on update, OK restart is required for DynamicEntities
        return obj;
    }
    
    /**
     * Remes given custom event and unregister it from eventListener servies 
     * @param <T>
     * @param event
     * @return
     */
    @Transactional(propagation = REQUIRES_NEW)
    public <T> CustomApplicationEvent<T> unregisterDynamicEntityEvent(CustomApplicationEvent<T> event) {
        debug("[unregisterDynamicEntityEvent] event [{}]", event);
        final String entityKeyWithSuffix = event.getName();
        Collection<CustomApplicationEvent> events = eventAndEntityMap.values().stream().filter( e -> e.getName().startsWith(entityKeyWithSuffix)).toList();
        repositories.unsecure.eventListener.findAll().stream().filter( el -> el.getEventName().startsWith(entityKeyWithSuffix)).forEach( e ->  { 
            services.eventListener.unregisterEventListenerClusterAware(e);
            debug("[unregisterDynamicEntityEvent] unregisterEventListenerClusterAware - event entry [{}]", e);
        });
        
        events.forEach( e -> {
            services.eventListener.unregisterCustomEventFromEventClass(e);        
            eventAndEntityMap.remove(e.getName());
            eventClassNameToClass.remove(e.getName());
            eventService.unregisterEventListeners(e);
            debug("[unregisterDynamicEntityEvent] unregistered from listener [{}]", e);
        });
        
        CustomApplicationEvent toRemove = repositories.unsecure.customEvent.findAll().stream().filter( e -> e.getName().equals(event.getName())).findFirst().orElse(null);
        if(toRemove != null) {
            repositories.unsecure.customEvent.delete(toRemove);
            debug("[unregisterDynamicEntityEvent] deleted event [{}]", toRemove);
        }
        
        return toRemove;
    }
    
    /**
     * For a entity-key and class (clazz) it registers EntityApplicationEvent in the listeners service to allow event publishing/subscriptions. It also updates customEvents maps (see members)
     * @param <T>
     * @param entityKey
     * @param clazz
     * @param suffix used to specify category of operation this event should be related to.  Pre-built categories are {@link EntityApplicationEvent.COMMON_CATEGORIES}
     */
    public <T> void registerEntityEvent(String entityKey, Class<T> clazz, String suffix) {
        debug("[registerEntityEvent] event for [entityKey : {}, class : {}, suffix : {}]", entityKey, clazz, suffix);
        final String entityKeyWithSuffix = entityKey + "_" + suffix;
        EntityApplicationEvent<T> eventType = CustomApplicationEvent.newInstance(EntityApplicationEvent.class, clazz, entityKey);
        eventType.setEventCategory(suffix);
        eventService.registerEventListener(eventType, this::debugCustomEvent);
        // it registers a class 
        services.eventListener.registerCustomEventFromEventClass(eventType, clazz);
        eventAndEntityMap.put(entityKeyWithSuffix, eventType);
        eventClassNameToClass.put(entityKeyWithSuffix, eventType.getClass());
        repositories.unsecure.eventListener.findAll().stream().filter( el -> el.getEventName().equals(entityKeyWithSuffix) ).forEach( e ->  { 
            e.setEventObjectType(clazz.getName());
            // it registers event listener
            services.eventListener.registerListenerClusterAware(e);
            debug("[registerEntityEvent] eventListener - registered [{}]", e);
        });
        
        
    }
    
    /**
     * based on a provided template event object register CustomApplicationEvent in EventLIstener service 
     * @param <T>
     * @param eventTemplate
     */
    public <T> void registerCustomEvent(CustomApplicationEvent<T> eventTemplate) {
        debug("[registerCustomEvent] event for [{}]", eventTemplate);
        if(eventTemplate.getClassName() != null && eventTemplate.getEventClass() == null) {
            try {
                eventTemplate.setEventClass((Class<T>) Class.forName(eventTemplate.getClassName()));
            } catch (ClassNotFoundException e) {
                warn("[registerCustomEvent] could not set event class in the event template, reason: {}", e.getMessage());
            }
        }
        eventService.registerEventListener(eventTemplate, this::debugCustomEvent);
        services.eventListener.registerCustomEventFromEventClass(eventTemplate, eventTemplate.getEventClass());
        eventAndEntityMap.put(eventTemplate.getEventName(), eventTemplate);
        eventClassNameToClass.put(eventTemplate.getEventName(), eventTemplate.getClass());
        repositories.unsecure.eventListener.findAll().stream().filter( 
                el -> 
                    el.getEventName().equals(eventTemplate.getEventName())
            ).forEach( e ->  { 
            e.setEventObjectType(eventTemplate.getEventClass().getName());
            services.eventListener.registerListenerClusterAware(e);
            debug("[registerCustomEvent] eventListener - registered [{}]", e);
        });
        
        
    }
    
    /**
     * Read all custom events from ORM and based on those template-event objects it performs registration. EntityApplicationEvents are not part of that loop, they are registers after Dynamic Entity's class creation done at a different stage via ByteBuddy.
     */
    public void registerAllEventListenersFromDb() {
        debug("[registerAllEventListenersFromDb] ");
        List<CustomApplicationEvent> customEvents = repositories.unsecure.customEvent.findAll().stream().filter( e -> !(e instanceof EntityApplicationEvent)).toList();
        customEvents.forEach( ce -> this.registerCustomEvent(ce));
    }
    
    <T> void debugCustomEvent(T eventObject) {
        debug("[debugCustomEvent] {}", eventObject.toString());
    }
    
    public Map<String, CustomApplicationEvent> getEventAndEntityMap() {
        return eventAndEntityMap;
    }

    public CustomApplicationEvent getEventTemplateFor(Object entity, String suffix) {
        debug("[getEventTemplateFor]");
        CustomApplicationEvent result = this.eventAndEntityMap.get(entity.toString() + "_" + suffix);
        if(result == null) {
            int index = entity.getClass().getSimpleName().lastIndexOf('_');
            String baseEntityClassName = entity.getClass().getSimpleName();
            if(index > 0) {
                baseEntityClassName = baseEntityClassName.substring(0, index);
            }
            
            final String baseEntityClassNameWithSuffix = StringUtils.uncapitalize(baseEntityClassName) + "_" + suffix;
            result = this.eventAndEntityMap.get(baseEntityClassNameWithSuffix);
        }
        
        
        return result;
    }
    
    /**
     * Shoud be executed only on a Dynamic Entity save operation 
     * @param entity
     * @return
     */
    public boolean onSave(Object entity) {
        return onEntityAction(entity, "CREATED");
    }
    
    /**
     * Shoud be executed only on a Dynamic Entity delete operation 
     * @param entity
     * @return
     */
    public boolean onDelete(Object entity) {
        return onEntityAction(entity, "DELETED");
    }
    
    /**
     * Shoud be executed only on a Dynamic Entity update operation 
     * @param entity
     * @return
     */
    public boolean onUpdate(Object entity) {
        onEntityAction(entity, "MODIFIED"); 
        return false;
    }

    /**
     * When a dynamic entity is beeing modified in any way, it emits an EntityApplicationEvent based on the type of altering action (CREATED, MODIFIED, DELETED)
     * @param entity
     */
    protected boolean onEntityAction(Object entity, String actionType) {
        debug("[onEntityAction] About to raise [{}-{}]", entity, actionType);
        EntityApplicationEvent<?> tempalteEvent = (EntityApplicationEvent<?>)getEventTemplateFor(entity, actionType);
        if(tempalteEvent != null) {
            try {
                EntityApplicationEvent<?> newEvent = (EntityApplicationEvent<?>) EntityApplicationEvent.newInstance(tempalteEvent.getClass(), entity.getClass(), tempalteEvent.getEventName(), entity);                
                eventService.emitEventAsync(newEvent);
                return true;
            } catch (SecurityException | IllegalArgumentException e) {
                e.printStackTrace();
                error("[onEntityAction] {}",e );
            } 
        }
        
        return false;
    }
}
