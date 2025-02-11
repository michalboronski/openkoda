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

import com.openkoda.dto.CanonicalObject;
import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;

/**
 * Event class dedicated for dynamic-entity actions. Default ones are {@link EntityApplicationEvent.COMMON_CATEGORIES} but it is possible to create own events raised in other cases than common CRUD operation form the refered list
 *
 * @author mboronski
 * @since 29-06-2024
 * @param <T> dynamic-entity class
 */
@Entity
@DiscriminatorValue("EntityApplicationEvent")
@Access(AccessType.PROPERTY)
public class EntityApplicationEvent<T> extends CustomApplicationEvent<T> {

    public static final String CREATED_CATEGORY = "CREATED";
    public static final String MODIFIED_CATEGORY = "MODIFIED";
    public static final String DELETED_CATEGORY = "DELETED";
    
    public static final Set<String> COMMON_CATEGORIES = Set.of(CREATED_CATEGORY, MODIFIED_CATEGORY, DELETED_CATEGORY);
    
    private static final long serialVersionUID = -8801850080888715749L;

    private String eventCategory;
    
    private static String extractEntity(String entityKey) {
        int index = entityKey.lastIndexOf('_');
        return index > 0 && COMMON_CATEGORIES.stream().anyMatch( e -> entityKey.endsWith(e)) ? entityKey.substring(0, index) : entityKey;
    }
    
    private static String extractCategory(String entityKey) {
        int index = entityKey.lastIndexOf('_');
        return index > 0 && COMMON_CATEGORIES.stream().anyMatch( e -> entityKey.endsWith(e)) ? entityKey.substring(index + 1, entityKey.length()) : null;
    }
    
    public EntityApplicationEvent() {
        super();
    }
    
    public EntityApplicationEvent(Class<T> eventClass, String entityKey) {
        super(eventClass, extractEntity(entityKey));
        this.eventCategory = extractCategory(entityKey);
    }

    public EntityApplicationEvent(Class<T> eventClass, String entityKey, T eventData) {
        super(eventClass, extractEntity(entityKey), eventData);
        this.eventCategory = extractCategory(entityKey);
    } 
    
    public EntityApplicationEvent(Class<T> eventClass, String entityKey, T eventData, String eventCategory) {
        super(eventClass, entityKey, eventData);
        this.eventCategory = eventCategory;
    } 
    
    @Column(name = "event_category")
    public String getEventCategory() {
        return eventCategory;
    }
    
    public void setEventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
    }
    
    @Override
    public String notificationMessage() {
        Object data = getEventData();
        if(this.getEventData() instanceof CanonicalObject) {
            data = ((CanonicalObject)this.getEventData()).notificationMessage();
        }
        
        String json = "{" + "\"object@" + getClassName() + "\":\"" + data + "\" }";
        return String.format("{\"eventName\":\"%s\","
                + "\"name\":\"%s\","
                + "\"eventData\":\"%s\","
                + "\"className\":\"%s\"}", this.getEventName(), this.getName(), json, this.getClassName());
    }
    
    @Override
    public String toString() {
        return this.getEventName();
    }
    
    @Override
    @Transient
    public String getEventName() {
        return String.format("%s_%s", getName(), getEventCategory() );
    }
    
    /**
     * The equals() method is used to compare two objects for equality.
     *
     * Two objects of this class are considered equal if they have the same eventClass and eventName fields
     * @param o object to compare
     * @return the result of a boolean comparison of whether objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityApplicationEvent<?> that = (EntityApplicationEvent<?>) o;
        return Objects.equals(eventClass, that.eventClass) &&
                Objects.equals(eventName, that.eventName) && Objects.equals(eventCategory, that.eventCategory);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(eventClass, eventName, eventCategory);
    }
}