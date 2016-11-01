package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;


/**
 * The institution search criteria.
 */
@Getter
@Setter
public class InstitutionSearchCriteria {
    /**
     * The institution name.
     */
    private String institutionName;

    /**
     * The parent organization.
     */
    private String parentOrganization;

    /**
     * The location.
     */
    private String location;

    /**
     * The email.
     */
    private String email;

    /**
     * The description.
     */
    private String description;

    /**
     * The institution status.
     */
    private InstitutionStatus status;
}

