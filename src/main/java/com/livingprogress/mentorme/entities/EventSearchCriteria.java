package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;


/**
 * The event search criteria.
 */
@Getter
@Setter
public class EventSearchCriteria {
    /**
     * The year.
     */
    private Integer year;

    /**
     * The month.
     */
    private Integer month;

    /**
     * The day and time.
     */
    private Date dayAndTime;

    /**
     * The event name.
     */
    private String name;

    /**
     * The institution id.
     */
    private Long institutionId;

    /**
     * The description.
     */
    private String description;

    /**
     * The global flag.
     */
    private Boolean global;

    /**
     * The invited mentors.
     */
    private List<Long> invitedMentors;

    /**
     * The invited mentees.
     */
    private List<Long> invitedMentees;

    /**
     * The upcoming flag.
     */
    private Boolean upcoming;
}

