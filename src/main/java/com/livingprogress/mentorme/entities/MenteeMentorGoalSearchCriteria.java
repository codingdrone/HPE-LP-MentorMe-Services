package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;


/**
 * The mentee goal search criteria.
 */
@Getter
@Setter
public class MenteeMentorGoalSearchCriteria {
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

