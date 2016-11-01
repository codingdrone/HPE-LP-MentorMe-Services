package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

/**
 * The goal.
 */
@Getter
@Setter
@Entity
public class Goal extends IdentifiableEntity {
    /**
     * The number of the goal in the goals list.
     */
    private int number;

    /**
     * The subject.
     */
    private String subject;

    /**
     * The description.
     */
    private String description;

    /**
     * The goal category.
     */
    @ManyToOne
    @JoinColumn(name = "goal_category_id")
    private GoalCategory goalCategory;

    /**
     * The duration in days.
     */
    private int durationInDays;

    /**
     * The tasks.
     */
    @OneToMany(mappedBy = "goalId")
    private List<Task> tasks;

    /**
     * The institutional program id.
     */
    @JoinColumn(name = "institutional_program_id")
    private long institutionalProgramId;

    /**
     * The useful links.
     */
    @ManyToMany
    @JoinTable(name = "goal_useful_link", joinColumns = {@JoinColumn(name = "goal_id")}, inverseJoinColumns = {@JoinColumn(name = "useful_link_id")})
    private List<UsefulLink> usefulLinks;

    /**
     * The documents.
     */
    @ManyToMany
    @JoinTable(name = "goal_document", joinColumns = {@JoinColumn(name = "goal_id")}, inverseJoinColumns = {@JoinColumn(name = "document_id")})
    private List<Document> documents;

    /**
     * Flag for the custom goal (the custom goal created for the mentee-mentor pair).
     */
    private boolean custom;

    /**
     * Custom assigned goal data.
     */
    @OneToOne(mappedBy = "goal")
    //  @OneToOne
    // @JoinColumn(name ="goal_id")
    private CustomAssignedGoalData customData;
}

