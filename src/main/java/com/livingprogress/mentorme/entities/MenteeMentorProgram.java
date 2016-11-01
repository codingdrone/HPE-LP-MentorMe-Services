package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import java.util.Date;
import java.util.List;

import static javax.persistence.TemporalType.DATE;

/**
 * The mentee-mentor program.
 */
@Getter
@Setter
@Entity
public class MenteeMentorProgram extends IdentifiableEntity {
    /**
     * The mentor.
     */
    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private Mentor mentor;

    /**
     * The mentee.
     */
    @ManyToOne
    @JoinColumn(name = "mentee_id")
    private Mentee mentee;

    /**
     * The institutional program.
     */
    @ManyToOne
    @JoinColumn(name = "institutional_program_id")
    private InstitutionalProgram institutionalProgram;

    /**
     * The mentor feedback.
     */
    @OneToOne
    @JoinColumn(name = "mentor_feedback_id")
    private MentorFeedback mentorFeedback;

    /**
     * The mentee feedback.
     */
    @OneToOne
    @JoinColumn(name = "mentee_feedback_id")
    private MenteeFeedback menteeFeedback;

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
     * The mentee mentor goals.
     */
    @OneToMany(mappedBy = "menteeMentorProgramId")
    private List<MenteeMentorGoal> goals;

    /**
     * The completed flag.
     */
    private boolean completed;

    /**
     * The completed on date.
     */
    private Date completedOn;

    /**
     * The request status.
     */
    @Enumerated(EnumType.STRING)
    private MenteeMentorProgramRequestStatus requestStatus;
}

