package com.livingprogress.mentorme.services.springdata;

import com.livingprogress.mentorme.entities.Mentee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * The Mentee repository.
 */
public interface MenteeRepository extends JpaRepository<Mentee,Long>, JpaSpecificationExecutor<Mentee> {
    /**
     * This method is used to get the the mentee by the parent consent token.
     * @param token the token
     * @return  the mentee
     */
    Mentee findByParentConsentToken(String token);
}

