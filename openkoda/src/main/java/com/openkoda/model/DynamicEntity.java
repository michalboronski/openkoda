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

package com.openkoda.model;

import com.openkoda.model.common.AuditableEntity;
import com.openkoda.model.common.ModelConstants;
import com.openkoda.model.common.TimestampedEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Collection;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"table_name"}))
public class DynamicEntity extends TimestampedEntity implements AuditableEntity {
    @Id
    @SequenceGenerator(name = GLOBAL_ID_GENERATOR, sequenceName = GLOBAL_ID_GENERATOR, initialValue = ModelConstants.INITIAL_GLOBAL_VALUE, allocationSize = 10)
    @GeneratedValue(generator = ModelConstants.GLOBAL_ID_GENERATOR, strategy = GenerationType.SEQUENCE)
    private Long id;
    @NotNull
    @Column(name = "table_name")
    private String tableName;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String toAuditString() {
        return tableName;
    }

    @Override
    public Collection<String> ignorePropertiesInAudit() {
        return AuditableEntity.super.ignorePropertiesInAudit();
    }

    @Override
    public Collection<String> contentProperties() {
        return AuditableEntity.super.contentProperties();
    }

}
