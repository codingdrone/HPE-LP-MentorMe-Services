package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;


/**
 * The institutional program search criteria.
 */
@Getter
@Setter
public class InstitutionalProgramSearchCriteria {
    /**
     * The program name.
     */
    private String programName;

    /**
     * The institution id.
     */
    private Long institutionId;

    /**
     * Get the program category.
     */
    private ProgramCategory programCategory;

    /**
     * The min duration in days.
     */
    private Integer minDurationInDays;

    /**
     * The max duration in days.
     */
    private Integer maxDurationInDays;
}

