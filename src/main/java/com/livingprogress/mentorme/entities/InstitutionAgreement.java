package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.List;

/**
 * The institution agreement.
 */
@Getter
@Setter
@Entity
public class InstitutionAgreement extends IdentifiableEntity {
    /**
     * The agreement name.
     */
    private String agreementName;

    /**
     * The agreement file path.
     */
    private String agreementFilePath;

    /**
     * The institution id.
     */
    @JoinColumn(name = "institution_id")
    private long institutionId;

    /**
     * The user roles.
     */
    @ManyToMany
    @JoinTable(name = "institution_agreement_user_role", joinColumns = {@JoinColumn(name = "institution_agreement_id")}, inverseJoinColumns = {@JoinColumn(name = "user_role_id")})
    private List<UserRole> userRoles;
}

