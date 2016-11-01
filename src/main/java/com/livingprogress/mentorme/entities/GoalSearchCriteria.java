package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;


/**
 * The goal search criteria.
 */
@Getter
@Setter
public class GoalSearchCriteria {
    /**
     * The program id.
     */
    private Long institutionalProgramId;

    /**
     * The description.
     */
    private String description;

    /**
     * The custom flag.
     */
    private Boolean custom;

    /**
     * The subject.
     */
    private String subject;
}

