package com.livingprogress.mentorme.services.springdata;

import com.livingprogress.mentorme.entities.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * The Mentor repository.
 */
public interface MentorRepository extends JpaRepository<Mentor,Long>, JpaSpecificationExecutor<Mentor> {
}

