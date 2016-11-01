package com.livingprogress.mentorme.services.springdata;

import com.livingprogress.mentorme.entities.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * The Institution repository.
 */
public interface InstitutionRepository extends JpaRepository<Institution,Long>, JpaSpecificationExecutor<Institution> {
}

