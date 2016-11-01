package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

/**
 * The mentor feedback.
 */
@Getter
@Setter
@Entity
public class MentorFeedback extends IdentifiableEntity {
    /**
     * The mentee score.
     */
    private int menteeScore;

    /**
     * The comment.
     */
    private String comment;
}

