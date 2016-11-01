package com.livingprogress.mentorme.services.springdata;

import com.livingprogress.mentorme.entities.InstitutionalProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * The InstitutionalProgram repository.
 */
public interface InstitutionalProgramRepository extends JpaRepository<InstitutionalProgram,Long>, JpaSpecificationExecutor<InstitutionalProgram> {
}

