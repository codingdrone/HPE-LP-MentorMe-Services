package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;


/**
 * The activity search criteria.
 */
@Getter
@Setter
public class ActivitySearchCriteria {
    /**
     * The institutional program id.
     */
    private Long institutionalProgramId;

    /**
     * The activity types.
     */
    private List<ActivityType> activityTypes;

    /**
     * The description.
     */
    private String description;

    /**
     * The start date.
     */
    private Date startDate;

    /**
     * The end date.
     */
    private Date endDate;

    /**
     * Mentee id.
     */
    private Long menteeId;

    /**
     * Mentor id.
     */
    private Long mentorId;

    /**
     * The global flag.
     */
    private Boolean global;
}

