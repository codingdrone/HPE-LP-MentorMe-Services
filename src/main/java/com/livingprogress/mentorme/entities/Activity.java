package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Temporal;
import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * The activity.
 */
@Getter
@Setter
@Entity
public class Activity extends IdentifiableEntity {
    /**
     * The institutional program id.
     */
    @JoinColumn(name = "institutional_program_id")
    private Long institutionalProgramId;

    /**
     * The activity type.
     */
    @Enumerated(EnumType.STRING)
    private ActivityType activityType;

    /**
     * The object id.
     */
    private long objectId;

    /**
     * The description.
     */
    private String description;

    /**
     * The created by.
     */
    @JoinColumn(name = "created_by", insertable = true, updatable = false)
    private long createdBy;

    /**
     * The created on.
     */
    @Column(name = "created_on", insertable = true, updatable = false)
    @Temporal(TIMESTAMP)
    private Date createdOn;

    /**
     * Mentee id.
     */
    @JoinColumn(name = "mentee_id")
    private Long menteeId;

    /**
     * Mentor id.
     */
    @JoinColumn(name = "mentor_id")
    private Long mentorId;

    /**
     * The global flag.
     */
    private boolean global;
}

