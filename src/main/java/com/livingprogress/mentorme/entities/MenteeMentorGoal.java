package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import java.util.Date;
import java.util.List;

import static javax.persistence.TemporalType.DATE;

/**
 * The mentee mentor goal.
 */
@Getter
@Setter
@Entity
public class MenteeMentorGoal extends IdentifiableEntity {
    /**
     * The goal.
     */
    @ManyToOne
    @JoinColumn(name = "goal_id")
    private Goal goal;

    /**
     * The completed flag.
     */
    private boolean completed;

    /**
     * The completed date.
     */
    private Date completedOn;

    /**
     * The goal's tasks.
     */
    @OneToMany(mappedBy = "menteeMentorGoalId")
    private List<MenteeMentorTask> tasks;

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
     * The mentee-mentor program id.
     */
    @JoinColumn(name = "mentee_mentor_program_id")
    private long menteeMentorProgramId;
}

