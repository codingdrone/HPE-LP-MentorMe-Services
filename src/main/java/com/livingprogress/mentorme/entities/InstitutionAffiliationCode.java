package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;

/**
 * The institution affiliation code.
 */
@Getter
@Setter
@Entity
public class InstitutionAffiliationCode extends IdentifiableEntity {
    /**
     * The institution affiliation code.
     */
    private String code;

    /**
     * The institution id.
     */
    @JoinColumn(name = "institution_id")
    private long institutionId;

    /**
     * The used flag.
     */
    private boolean used;
}

