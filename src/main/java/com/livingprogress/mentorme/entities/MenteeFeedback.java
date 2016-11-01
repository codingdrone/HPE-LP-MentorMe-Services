package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

/**
 * The mentee feedback.
 */
@Getter
@Setter
@Entity
public class MenteeFeedback extends IdentifiableEntity {
    /**
     * The mentor score.
     */
    private int mentorScore;

    /**
     * The comment.
     */
    private String comment;
}

