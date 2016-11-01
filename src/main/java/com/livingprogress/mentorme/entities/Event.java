package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import java.util.Date;
import java.util.List;

import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * The event.
 */
@Getter
@Setter
@Entity
public class Event extends IdentifiableEntity {
    /**
     * The institution id.
     */
    @JoinColumn(name = "institution_id")
    private long institutionId;

    /**
     * The name.
     */
    private String name;

    /**
     * The description.
     */
    private String description;

    /**
     * The start time.
     */
    @Temporal(TIMESTAMP)
    private Date startTime;

    /**
     * The end time.
     */
    @Temporal(TIMESTAMP)
    private Date endTime;

    /**
     * The event location.
     */
    private String eventLocation;

    /**
     * The event location address
     */
    private String eventLocationAddress;

    /**
     * The city.
     */
    private String city;

    /**
     * The state.
     */
    @ManyToOne
    @JoinColumn(name = "state_id")
    private State state;

    /**
     * The zip code.
     */
    private String zip;

    /**
     * The country.
     */
    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    /**
     * The created by
     */
    @Column(name = "created_by", insertable = true, updatable = false)
    @JoinColumn(name = "created_by", insertable = true, updatable = false)
    private long createdBy;

    /**
     * The invited mentors
     */
    @ManyToMany
    @JoinTable(name = "event_invited_mentor", joinColumns = {@JoinColumn(name = "event_id")}, inverseJoinColumns = {@JoinColumn(name = "mentor_id")})
    private List<Mentor> invitedMentors;

    /**
     * The invited mentees
     */
    @ManyToMany
    @JoinTable(name = "event_invited_mentee", joinColumns = {@JoinColumn(name = "event_id")}, inverseJoinColumns = {@JoinColumn(name = "mentee_id")})
    private List<Mentee> invitedMentees;

    /**
     * The global flag
     */
    private boolean global;
}

