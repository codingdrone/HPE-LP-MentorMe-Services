package com.livingprogress.mentorme.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import java.util.Date;

import static javax.persistence.TemporalType.DATE;

/**
 * The professional experience data.
 */
@Getter
@Setter
@Entity
public class ProfessionalExperienceData extends IdentifiableEntity {
    /**
     * The position.
     */
    private String position;

    /**
     * The work location.
     */
    private String workLocation;

    /**
     * The start date.
     */
    @Temporal(DATE)
    private Date startDate;

    /**
     * The end date.
     */
    @Temporal(DATE)
    private Date endDate;

    /**
     * The description.
     */
    private String description;

    /**
     * The mentor id.
     */
    @Column(name = "mentor_id", insertable = false, updatable = false)
    private long mentorId;

    /**
     * The mentor.
     */
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private Mentor mentor;
}

