package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


/**
 * The mentee-mentor program search criteria.
 */
@Getter
@Setter
public class MenteeMentorProgramSearchCriteria {
    /**
     * The mentor id.
     */
    private Long mentorId;

    /**
     * The mentee id.
     */
    private Long menteeId;

    /**
     * The institutional program id.
     */
    private Long institutionalProgramId;

    /**
     * The start date.
     */
    private Date startDate;

    /**
     * The end date.
     */
    private Date endDate;

    /**
     * The completed flag.
     */
    private Boolean completed;

    /**
     * The request status.
     */
    private MenteeMentorProgramRequestStatus requestStatus;
}

