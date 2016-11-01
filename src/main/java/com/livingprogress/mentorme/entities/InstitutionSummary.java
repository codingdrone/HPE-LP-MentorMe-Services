package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * The institution summary.
 */
@Getter
@Setter
public class InstitutionSummary {
    /**
     * The programs count.
     */
    private long institutionalProgramsCount;

    /**
     * The upcoming events count.
     */
    private long upcomingEventsCount;

    /**
     * The associated mentees count.
     */
    private long associatedMenteesCount;

    /**
     * The associated mentors count.
     */
    private long associatedMentorsCount;
}

