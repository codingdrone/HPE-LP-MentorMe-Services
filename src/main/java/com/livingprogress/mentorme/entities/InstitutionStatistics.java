package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * The institution statistics.
 */
@Getter
@Setter
public class InstitutionStatistics {
    /**
     * The statistics by country.
     */
    private Map<Country, Integer> byCountry;
}

