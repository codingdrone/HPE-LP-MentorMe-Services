package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Temporal;
import java.util.Date;

import static javax.persistence.TemporalType.DATE;

/**
 * The responsibility.
 */
@Getter
@Setter
@Entity
public class Responsibility extends IdentifiableEntity {
    /**
     * The number in the list.
     */
    private int number;

    /**
     * The title
     */
    private String title;

    /**
     * The date
     */
    @Temporal(DATE)
    private Date date;

    /**
     * The mentee responsibility.
     */
    private boolean menteeResponsibility;

    /**
     * The mentor responsibility.
     */
    private boolean mentorResponsibility;

    /**
     * The institutional program id.
     */
    @JoinColumn(name = "institutional_program_id")
    private long institutionalProgramId;
}

