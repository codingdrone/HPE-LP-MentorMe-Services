package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;


/**
 * The institution agreement search criteria.
 */
@Getter
@Setter
public class InstitutionAgreementSearchCriteria {
    /**
     * The agreement name.
     */
    private String agreementName;

    /**
     * The institution id.
     */
    private Long institutionId;

    /**
     * The user role.
     */
    private UserRole userRole;
}

