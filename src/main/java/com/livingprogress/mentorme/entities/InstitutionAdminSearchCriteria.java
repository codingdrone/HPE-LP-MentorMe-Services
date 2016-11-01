package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * The institution admin search criteria.
 */
@Getter
@Setter
public class InstitutionAdminSearchCriteria extends UserSearchCriteria {
    /**
     * The institution id.
     */
    private Long institutionId;
}

