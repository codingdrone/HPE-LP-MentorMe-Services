package com.livingprogress.mentorme.services.springdata;

import com.livingprogress.mentorme.entities.ProfessionalInterest;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The ProfessionalInterest repository.
 */
public interface ProfessionalInterestRepository extends JpaRepository<ProfessionalInterest,Long> {
}

