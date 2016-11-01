package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * The overall statistics.
 */
@Getter
@Setter
public class OverallStatistics {
    /**
     * The institutions count.
     */
    private int institutionsCount;

    /**
     * The institutional programs count.
     */
    private int institutionalProgramCount;

    /**
     * The mentees count.
     */
    private int menteesCount;

    /**
     * The mentros count.
     */
    private int mentorsCount;
}

