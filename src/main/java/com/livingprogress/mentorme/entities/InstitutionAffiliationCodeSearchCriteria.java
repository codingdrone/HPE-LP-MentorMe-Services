package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;


/**
 * The institution affiliation code search criteria.
 */
@Getter
@Setter
public class InstitutionAffiliationCodeSearchCriteria {
    /**
     * The code.
     */
    private String code;

    /**
     * The institution id.
     */
    private Long institutionId;

    /**
     * The used flag.
     */
    private Boolean used;
}

