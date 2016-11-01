package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;
/**
 * The mentee search criteria.
 */
@Getter
@Setter
public class MenteeSearchCriteria extends InstitutionUserSearchCriteria {
    /**
     * The school name.
     */
    private String schoolName;
}

