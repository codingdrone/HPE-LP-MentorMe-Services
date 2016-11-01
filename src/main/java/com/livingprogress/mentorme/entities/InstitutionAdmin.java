package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;

/**
 * The institution admin.
 */
@Getter
@Setter
@Entity
public class InstitutionAdmin extends User {
    /**
     * The institution id.
     */
    @JoinColumn(name = "institution_id")
    private long institutionId;

    /**
     * The title.
     */
    private String title;
}

