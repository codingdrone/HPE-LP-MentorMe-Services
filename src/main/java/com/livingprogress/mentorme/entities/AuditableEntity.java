package com.livingprogress.mentorme.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * The auditable entity.
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
public abstract class AuditableEntity extends IdentifiableEntity {
    /**
     * The created on date.
     */
    @Column(name = "created_on", insertable = true, updatable = false)
    @Temporal(TIMESTAMP)
    private Date createdOn;
}

