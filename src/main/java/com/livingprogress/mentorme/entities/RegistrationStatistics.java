package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;

import java.time.Month;
import java.util.Map;

/**
 * The registration statistics.
 */
@Getter
@Setter
public class RegistrationStatistics {
    /**
     * The year.
     */
    private int year;

    /**
     * The institutions distribution.
     */
    private Map<Month, Integer> institutions;

    /**
     * The institutional programs distribution.
     */
    private Map<Month, Integer> institutionalPrograms;

    /**
     * The mentees distribution.
     */
    private Map<Month, Integer> mentees;

    /**
     * The mentors distribution.
     */
    private Map<Month, Integer> mentors;
}

