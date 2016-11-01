package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;


/**
 * The task search criteria.
 */
@Getter
@Setter
public class TaskSearchCriteria {
    /**
     * The goal id.
     */
    private Long goalId;

    /**
     * The description.
     */
    private String description;

    /**
     * The custom flag.
     */
    private Boolean custom;
}

