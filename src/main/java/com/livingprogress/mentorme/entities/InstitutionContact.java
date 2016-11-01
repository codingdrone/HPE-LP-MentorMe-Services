package com.livingprogress.mentorme.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * The institution contact.
 */
@Getter
@Setter
@Entity
public class InstitutionContact extends IdentifiableEntity {

    /**
     * The title.
     */
    private String title;

    /**
     * The first name.
     */
    private String firstName;

    /**
     * The last name.
     */
    private String lastName;

    /**
     * The email.
     */
    private String email;

    /**
     * The phone number.
     */
    private String phoneNumber;

    /**
     * Is contact primary.
     */
    private boolean primaryContact;

    /**
     * The id of match institution.
     */
    @ManyToOne
    @JoinColumn(name = "institution_id")
    @JsonIgnore
    private Institution institution;
}

