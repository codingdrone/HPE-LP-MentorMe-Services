package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * The institution admin access request.
 */
@Getter
@Setter
@Entity
public class InstitutionAdminAccessRequest extends IdentifiableEntity {
    /**
     * The institution admin.
     */
    @ManyToOne
    @JoinColumn(name = "institution_admin_id")
    private InstitutionAdmin institutionAdmin;

    /**
     * The institution.
     */
    @ManyToOne
    @JoinColumn(name = "institution_id")
    private Institution institution;

    /**
     * The status.
     */
    @Enumerated(EnumType.STRING)
    private InstitutionAdminAccessRequestStatus status;
}

