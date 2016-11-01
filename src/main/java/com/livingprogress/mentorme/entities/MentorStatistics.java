package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * The mentor statistics.
 */
@Getter
@Setter
public class MentorStatistics {
    /**
     * The statistics by country.
     */
    private Map<Country, Integer> byCountry;

    /**
     * The statistics by age.
     */
    private Map<String, Integer> byAge;

    /**
     * The statistics by rating.
     */
    private Map<Country, Integer> byRating;
}

