package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.math.BigDecimal;

/**
 * The mentee.
 */
@Getter
@Setter
@Entity
public class Mentee extends InstitutionUser {
    /**
     * The family income.
     */
    private BigDecimal familyIncome;

    /**
     * The school.
     */
    private String school;

    /**
     * The institution affiliation code.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "institution_affiliation_code_id")
    private InstitutionAffiliationCode institutionAffiliationCode;

    /**
     * The parent consent.
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_consent_id")
    private ParentConsent parentConsent;

    /**
     * The facebook url.
     */
    private String facebookUrl;
}

