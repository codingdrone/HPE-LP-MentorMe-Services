package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

/**
 * The parent consent.
 */
@Getter
@Setter
@Entity
public class ParentConsent extends IdentifiableEntity {
    /**
     * The parent name.
     */
    private String parentName;

    /**
     * The signature file path.
     */
    private String signatureFilePath;

    /**
     * The parent email.
     */
    private String parentEmail;

    /**
     * The confirmation token.
     */
    private String token;
}

