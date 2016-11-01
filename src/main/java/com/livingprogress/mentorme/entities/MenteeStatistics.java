package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * The mentee statistics.
 */
@Getter
@Setter
public class MenteeStatistics {
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

