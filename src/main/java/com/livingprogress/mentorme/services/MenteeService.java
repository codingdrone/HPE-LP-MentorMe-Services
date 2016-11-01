package com.livingprogress.mentorme.services;

import com.livingprogress.mentorme.entities.Mentee;
import com.livingprogress.mentorme.entities.MenteeSearchCriteria;
import com.livingprogress.mentorme.exceptions.EntityNotFoundException;
import com.livingprogress.mentorme.exceptions.MentorMeException;

import java.util.List;

/**
 * The mentee service. Extends generic service interface.Implementation should be effectively thread-safe.
 */
public interface MenteeService extends GenericService<Mentee, MenteeSearchCriteria> {
    /**
     * This method is used to get the program mentees.
     *
     * @param programId the mentee mentor program id.
     * @return the match program mentees.
     * @throws IllegalArgumentException if programId is not positive
     * @throws MentorMeException if any other error occurred during operation
     */
    List<Mentee> getProgramMentees(long programId) throws MentorMeException;

    /**
     * This method is used to get the mentee avg score.
     *
     * @param id the id of the entity to retrieve
     * @return the avg score
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws MentorMeException if any other error occurred during operation
     */
    int getAverageMenteeScore(long id) throws MentorMeException;

    /**
     * This method is used to confirm the parent consent.
     *
     * @param token the token
     * @return true if found parent consent for inactive mentee
     * @throws IllegalArgumentException if token is null or empty
     * @throws MentorMeException if any other error occurred during operation
     */
    boolean confirmParentConsent(String token) throws MentorMeException;
}

