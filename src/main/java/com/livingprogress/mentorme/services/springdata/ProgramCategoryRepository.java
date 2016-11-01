package com.livingprogress.mentorme.services.springdata;

import com.livingprogress.mentorme.entities.ProgramCategory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The ProgramCategory repository.
 */
public interface ProgramCategoryRepository extends JpaRepository<ProgramCategory,Long> {
}

