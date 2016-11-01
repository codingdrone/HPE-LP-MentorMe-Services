package com.livingprogress.mentorme.services;

import com.livingprogress.mentorme.entities.Country;
import com.livingprogress.mentorme.entities.DocumentCategory;
import com.livingprogress.mentorme.entities.GoalCategory;
import com.livingprogress.mentorme.entities.PersonalInterest;
import com.livingprogress.mentorme.entities.ProfessionalConsultantArea;
import com.livingprogress.mentorme.entities.ProfessionalInterest;
import com.livingprogress.mentorme.entities.ProgramCategory;
import com.livingprogress.mentorme.entities.State;
import com.livingprogress.mentorme.entities.UserRole;
import com.livingprogress.mentorme.exceptions.MentorMeException;

import java.util.List;

/**
 * The lookup service.Implementation should be effectively thread-safe.
 */
public interface LookupService {
    /**
     * This method is used to get user role lookups.
     *
     * @return the lookups for user role.
     * @throws MentorMeException if any other error occurred during operation
     */
   List<UserRole> getUserRoles() throws MentorMeException;

    /**
     * This method is used to get country lookups.
     *
     * @return the lookups for country.
     * @throws MentorMeException if any other error occurred during operation
     */
    List<Country> getCountries() throws MentorMeException;

    /**
     * This method is used to get state lookups.
     *
     * @return the lookups for state.
     * @throws MentorMeException if any other error occurred during operation
     */
    List<State> getStates() throws MentorMeException;

    /**
     * This method is used to get professional consultant area lookups.
     *
     * @return the lookups for professional consultant area.
     * @throws MentorMeException if any other error occurred during operation
     */
    List<ProfessionalConsultantArea> getProfessionalConsultantAreas() throws MentorMeException;

    /**
     * This method is used to get goal category lookups.
     *
     * @return the lookups for goal category
     * @throws MentorMeException if any other error occurred during operation
     */
    List<GoalCategory> getGoalCategories() throws MentorMeException;

    /**
     * This method is used to program category lookups.
     *
     * @return the lookups for program category
     * @throws MentorMeException if any other error occurred during operation
     */
    List<ProgramCategory> getProgramCategories() throws MentorMeException;

    /**
     * This method is used to personal interest lookups.
     *
     * @return the lookups for personal interest
     * @throws MentorMeException if any other error occurred during operation
     */
    List<PersonalInterest> getPersonalInterests() throws MentorMeException;

    /**
     * This method is used to professional interest lookups.
     *
     * @return the lookups for professional interest
     * @throws MentorMeException if any other error occurred during operation
     */
    List<ProfessionalInterest> getProfessionalInterests() throws MentorMeException;

    /**
     * This method is used to document category lookups.
     *
     * @return the lookups for document category
     * @throws MentorMeException if any other error occurred during operation
     */
    List<DocumentCategory> getDocumentCategories() throws MentorMeException;
}

