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
import com.openkoda.model.Privilege;
import com.openkoda.model.common.AuditableEntity;
import com.openkoda.model.common.LongIdEntity;
import com.openkoda.model.common.ModelConstants;
import com.openkoda.model.common.SearchableEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "custom_event")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@Access(AccessType.PROPERTY)
public class CustomApplicationEvent<T> extends AbstractApplicationEvent<T> implements LongIdEntity, AuditableEntity, Serializable, SearchableEntity, ModelConstants, CanonicalObject {

    private static final long serialVersionUID = 9160914552022771702L;
    
    private Long id;
    
    private String name;

    private LocalDateTime updatedOn;

    private Privilege readPrivilege;
    
    private Privilege writePrivilege;
    
    private T eventData;
    
    private String indexString;
    
    protected String className;
    
    public CustomApplicationEvent() {
        super(null, null);
        className = null;
    }
    
    public CustomApplicationEvent(Class<T> eventClass, String eventName) {
        super(eventClass, eventName);
        this.name = eventName;
        this.className = eventClass.getName();
    }
    
    public CustomApplicationEvent(Class<T> eventClass, String eventName, T eventData) {
        super(eventClass, eventName);
        className = eventClass != null ? eventClass.getName() : null;
        this.name = eventName;
        this.eventData = eventData;
    }
    
    @Id
    @SequenceGenerator(name = GLOBAL_ID_GENERATOR, sequenceName = GLOBAL_ID_GENERATOR, initialValue = ModelConstants.INITIAL_GLOBAL_VALUE, allocationSize = 10)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IdOrGenerated")
    @GenericGenerator(name="IdOrGenerated", strategy="com.openkoda.core.customisation.UseIdOrGenerate")
    @Override
    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    @LastModifiedDate
    @Column(name = UPDATED_ON, columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP", insertable=false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }
    
    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }
    
    //  read privilege for the registered form
    @Column(name = "read_privilege")
    @Enumerated(EnumType.STRING)
    public Privilege getReadPrivilege() {
        return readPrivilege;
    }

    public void setReadPrivilege(Privilege readPrivilege) {
        this.readPrivilege = readPrivilege;
    }

    //  write privilege for the registered form
    @Column(name = "write_privilege")
    @Enumerated(EnumType.STRING)
    public Privilege getWritePrivilege() {
        return writePrivilege;
    }

    public void setWritePrivilege(Privilege writePrivilege) {
        this.writePrivilege = writePrivilege;
    }
    
    @Transient
    public T getEventData() {
        return eventData;
    }
    
    public void setEventData(T eventData) {
        this.eventData = eventData;
    }
    
    @Transient
    public String getEventName() {
        return this.eventName;
    }
    
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
    
    @Column(name = "name", nullable = false)
    public String getName() {
        return this.name;
    }
    
    public void setName(String eventName) {
        this.name = eventName;
        if(this.eventName == null) {
            this.eventName = eventName;
        }
    }
    
    @Override
    public String toAuditString() {
        return String.format("%s-%s-%s : %s", getClass().getSimpleName(), getName(), getEventData() != null ? getEventData().getClass().getSimpleName() : "", getEventData() != null ? getEventData() : "[no data]");
    }
    
    @Override
    public String toString() {
        return this.getName();
    }
    
    public static <K, E extends CustomApplicationEvent> E newInstance(Class<E> eventClass, Class<K> clazz, String name) {
        try {
            Constructor<E> constructor = eventClass.getDeclaredConstructor(Class.class, String.class);
            return constructor.newInstance(clazz, name);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static <K, E extends CustomApplicationEvent> CustomApplicationEvent<K> newInstance(Class<E> eventClass, Class<K> clazz, String name, Object data) {
        try {
            Constructor<E> constructor = eventClass.getDeclaredConstructor(Class.class, String.class, Object.class);
            return constructor.newInstance(clazz, name, (K)data);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transient
    public Class<T> getEventClass() {
        return this.eventClass;
    }

    public void setEventClass(Class<T> clazz) {
        this.eventClass = clazz;
    }
    
    @Column(name = "class_name")
    public String getClassName() {
        return className;
    }
    
    public void setClassName(String className) {
        this.className = className;
    }

    @Column(name = INDEX_STRING_COLUMN, length = INDEX_STRING_COLUMN_LENGTH, insertable = false)
    @ColumnDefault("''")
    @Override
    public String getIndexString() {
        // TODO Auto-generated method stub
        return "";
    }
    
    public void setIndexString(String indexString) {
        this.indexString = indexString;
    }

    @Override
    public String notificationMessage() {
        return String.format("{\"eventName\":\"%s\","
                + "\"name\":\"%s\","
                + "\"eventData\":\"%s\","
                + "\"className\":\"%s\"}", this.getEventName(), this.getName(), this.getEventData(), this.getClassName());
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
        AbstractApplicationEvent<?> that = (AbstractApplicationEvent<?>) o;
        return Objects.equals(eventClass, that.eventClass) &&
                Objects.equals(eventName, that.eventName);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(eventClass, eventName);
    }
}