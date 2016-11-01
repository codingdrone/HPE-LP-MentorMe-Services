package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import java.util.Date;
import java.util.List;

import static javax.persistence.TemporalType.DATE;

/**
 * The institutional program.
 */
@Getter
@Setter
@Entity
public class InstitutionalProgram extends AuditableEntity {
    /**
     * The program name.
     */
    private String programName;

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
     * The institution.
     */
    @ManyToOne
    @JoinColumn(name = "institution_id")
    private Institution institution;

    /**
     * The program category.
     */
    @ManyToOne
    @JoinColumn(name = "program_category_id")
    private ProgramCategory programCategory;

    /**
     * The goals.
     */
    @OneToMany(mappedBy = "institutionalProgramId")
    private List<Goal> goals;

    /**
     * The responsibilities.
     */
    @OneToMany(mappedBy = "institutionalProgramId")
    private List<Responsibility> responsibilities;

    /**
     * The documents.
     */
    @ManyToMany
    @JoinTable(name = "institutional_program_document", joinColumns = {@JoinColumn(name = "institutional_program_id")}, inverseJoinColumns = {@JoinColumn(name = "document_id")})
    private List<Document> documents;

    /**
     * The useful links.
     */
    @ManyToMany
    @JoinTable(name = "institutional_program_link", joinColumns = {@JoinColumn(name = "institutional_program_id")}, inverseJoinColumns = {@JoinColumn(name = "useful_link_id")})
    private List<UsefulLink> usefulLinks;

    /**
     * The duration in days.
     */
    private int durationInDays;
}

