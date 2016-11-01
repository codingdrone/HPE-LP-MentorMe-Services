package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

/**
 * The useful link.
 */
@Getter
@Setter
@Entity
public class UsefulLink extends IdentifiableEntity {
    /**
     * The title.
     */
    private String title;

    /**
     * The link address.
     */
    private String address;
}

