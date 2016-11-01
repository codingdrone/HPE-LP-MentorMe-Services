package com.livingprogress.mentorme.services.springdata;

import com.livingprogress.mentorme.entities.Mentee;
import com.livingprogress.mentorme.entities.MenteeMentorProgram;
import com.livingprogress.mentorme.entities.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * The MenteeMentorProgram repository.
 */
public interface MenteeMentorProgramRepository extends JpaRepository<MenteeMentorProgram,Long>, JpaSpecificationExecutor<MenteeMentorProgram> {
    /**
     * This method is used to get the mentees for the program
     * @param programId the program id
     * @return the mentees
     */
    @Query("SELECT distinct p.mentee FROM MenteeMentorProgram p WHERE p.institutionalProgram.id = :programId")
    List<Mentee> getProgramMentees(@Param(value = "programId") long programId);

    /**
     * This method is used to get the mentors for the program
     * @param programId the program id
     * @return the mentors.
     */
    @Query("SELECT distinct p.mentor FROM MenteeMentorProgram p WHERE p.institutionalProgram.id = :programId")
    List<Mentor> getProgramMentors(@Param(value = "programId") long programId);

    /**
     * This method is used to get the mentee average score.
     * @param menteeId the mentee id
     * @return the average score.
     */
    @Query("SELECT FLOOR(AVG(p.mentorFeedback.menteeScore)) FROM MenteeMentorProgram p WHERE p.mentee.id = :menteeId")
    int getAverageMenteeScore(@Param(value = "menteeId") long menteeId);

    /**
     * This method is used to get the mentor average score.
     * @param  mentorId the mentor id
     * @return the average score.
     */
    @Query("SELECT FLOOR(AVG(p.menteeFeedback.mentorScore)) FROM MenteeMentorProgram p WHERE p.mentor.id = :mentorId")
    int getAverageMentorScore(@Param(value = "mentorId") long mentorId);
}

