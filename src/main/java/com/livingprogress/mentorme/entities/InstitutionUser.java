package com.livingprogress.mentorme.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import java.util.Date;
import java.util.List;

import static javax.persistence.TemporalType.DATE;

/**
 * The institution user.
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
public abstract class InstitutionUser extends User {
    /**
     * The institution.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "institution_id")
    private Institution institution;

    /**
     * The assigned to institution flag.
     */
    private boolean assignedToInstitution;

    /**
     * The birth date.
     */
    @Temporal(DATE)
    private Date birthDate;

    /**
     * The phone.
     */
    private String phone;

    /**
     * The skype username.
     */
    private String skypeUsername;

    /**
     * The intro video link.
     */
    private String introVideoLink;

    /**
     * The description.
     */
    private String description;

    /**
     * The personal interests.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WeightedPersonalInterest> personalInterests;

    /**
     * The professional interests.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WeightedProfessionalInterest> professionalInterests;

    /**
     * The average performance score.
     */
    private int averagePerformanceScore;
}

