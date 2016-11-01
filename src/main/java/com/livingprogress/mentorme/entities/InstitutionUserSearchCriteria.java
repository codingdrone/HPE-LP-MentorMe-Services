package com.livingprogress.mentorme.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

/**
 * The institution user search criteria.
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class InstitutionUserSearchCriteria {
    /**
     * The institution id.
     */
    private Long institutionId;

    /**
     * The status.
     */
    private UserStatus status;

    /**
     * The min average performance score.
     */
    private Integer minAveragePerformanceScore;

    /**
     * The max average performance score.
     */
    private Integer maxAveragePerformanceScore;

    /**
     * The name.
     */
    private String name;

    /**
     * The personal interest.
     */
    private List<PersonalInterest> personalInterests;

    /**
     * The professional interest.
     */
    private List<ProfessionalInterest> professionalInterests;

    /**
     * The assigned to institution flag.
     */
    private Boolean assignedToInstitution;
}

