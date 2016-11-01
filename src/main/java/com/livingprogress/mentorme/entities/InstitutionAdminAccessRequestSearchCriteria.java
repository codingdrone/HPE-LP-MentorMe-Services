package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * The admin access request search criteria.
 */
@Getter
@Setter
public class InstitutionAdminAccessRequestSearchCriteria {
    /**
     * The status.
     */
    private InstitutionAdminAccessRequestStatus status;
}

