package com.livingprogress.mentorme.services.springdata;

import com.livingprogress.mentorme.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The Country repository.
 */
public interface CountryRepository extends JpaRepository<Country,Long> {
}

