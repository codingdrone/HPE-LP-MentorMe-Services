package com.livingprogress.mentorme.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * The custom assigned data.
 */
@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class CustomAssignedData extends IdentifiableEntity {
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
}

