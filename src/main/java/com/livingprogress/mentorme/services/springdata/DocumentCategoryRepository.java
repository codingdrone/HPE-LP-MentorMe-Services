package com.livingprogress.mentorme.services.springdata;

import com.livingprogress.mentorme.entities.DocumentCategory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The InstitutionalProgram repository.
 */
public interface DocumentCategoryRepository extends JpaRepository<DocumentCategory,Long> {
}

