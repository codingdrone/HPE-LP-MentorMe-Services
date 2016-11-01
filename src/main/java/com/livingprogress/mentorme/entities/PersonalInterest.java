package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * The personal interest.
 */
@Getter
@Setter
@Entity
public class PersonalInterest extends LookupEntity {
    /**
     * The picture path.
     */
    private String picturePath;

    /**
     * The parent category
     */
    @ManyToOne
    @JoinColumn(name = "parent_category_id")
    private PersonalInterest parentCategory;
}

