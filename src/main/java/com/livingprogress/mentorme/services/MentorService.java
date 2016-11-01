package com.livingprogress.mentorme.services;

import com.livingprogress.mentorme.entities.Mentor;
import com.livingprogress.mentorme.entities.MentorSearchCriteria;
import com.livingprogress.mentorme.exceptions.EntityNotFoundException;
import com.livingprogress.mentorme.exceptions.MentorMeException;

import java.util.List;

/**
 * The mentor service. Extends generic service interface.Implementation should be effectively thread-safe.
 */
public interface MentorService extends GenericService<Mentor, MentorSearchCriteria> {
    /**
     * This method is used to get the program mentors.
     *
     * @param programId the mentee mentor program id.
     * @return the match program mentors.
     * @throws IllegalArgumentException if programId is not positive
     * @throws MentorMeException if any other error occurred during operation
     */
    List<Mentor> getProgramMentors(long programId) throws MentorMeException;

    /**
     * This method is used to get the mentor avg score.
     *
     * @param id the id of the entity to retrieve
     * @return the avg score
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws MentorMeException if any other error occurred during operation
     */
    int getAverageMentorScore(long id) throws MentorMeException;
}

