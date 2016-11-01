package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;


/**
 * The mentee task.
 */
@Getter
@Setter
@Entity
public class MenteeMentorTask extends IdentifiableEntity {
    /**
     * The task.
     */
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    /**
     * The completed flag.
     */
    private boolean completed;

    /**
     * The completed on.
     */
    @Temporal(TIMESTAMP)
    private Date completedOn;

    /**
     * The mentee mentor goal id.
     */
    @JoinColumn(name = "mentee_mentor_goal_id")
    private long menteeMentorGoalId;
}

