package com.livingprogress.mentorme.services.springdata;

import com.livingprogress.mentorme.entities.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * The Institution repository.
 */
public interface InstitutionRepository extends JpaRepository<Institution,Long>, JpaSpecificationExecutor<Institution> {

    /**
     * This method is used to get the mentees for the program
     * @param programId the program id
     * @return the mentees
     */
  //  @Query("SELECT distinct p.mentee FROM MenteeMentorProgram p WHERE p.institutionalProgram.id = :programId")
   // void removeAll(@Param(value = "institutionId") long institutionId);
}

