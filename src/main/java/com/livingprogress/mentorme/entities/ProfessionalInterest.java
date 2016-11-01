package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * The professional interest.
 */
@Getter
@Setter
@Entity
public class ProfessionalInterest extends LookupEntity {
    /**
     * The picture path.
     */
    private String picturePath;

    /**
     * The parent category
     */
    @ManyToOne
    @JoinColumn(name = "parent_category_id")
    private ProfessionalInterest parentCategory;
}

