package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Enumerated;
import java.util.List;

/**
 * The mentor search criteria.
 */
@Getter
@Setter
public class MentorSearchCriteria extends InstitutionUserSearchCriteria {
    /**
     * The mentor type.
     */
    private MentorType mentorType;

    /**
     * The professional areas.
     */
    private List<ProfessionalConsultantArea> professionalAreas;

    /**
     * The company name.
     */
    private String companyName;

    /**
     * The mentor request status.
     */
    private MenteeMentorProgramRequestStatus mentorRequestStatus;
}

