package com.livingprogress.mentorme.services.springdata;

import com.livingprogress.mentorme.entities.PersonalInterest;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The PersonalInterest repository.
 */
public interface PersonalInterestRepository extends JpaRepository<PersonalInterest,Long> {
}

