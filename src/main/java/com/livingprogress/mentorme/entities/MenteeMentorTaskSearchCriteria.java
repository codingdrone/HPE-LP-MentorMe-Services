package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;


/**
 * The mentee task search criteria.
 */
@Getter
@Setter
public class MenteeMentorTaskSearchCriteria {
    /**
     * The program id.
     */
    private Long menteeMentorProgramId;

    /**
     * The completed flag.
     */
    private Boolean completed;

    /**
     * The mentee id.
     */
    private Long menteeId;
}

