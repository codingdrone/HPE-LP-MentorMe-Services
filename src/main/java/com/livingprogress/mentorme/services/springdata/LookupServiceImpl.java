package com.livingprogress.mentorme.services.springdata;

import com.livingprogress.mentorme.entities.Country;
import com.livingprogress.mentorme.entities.DocumentCategory;
import com.livingprogress.mentorme.entities.GoalCategory;
import com.livingprogress.mentorme.entities.PersonalInterest;
import com.livingprogress.mentorme.entities.ProfessionalConsultantArea;
import com.livingprogress.mentorme.entities.ProfessionalInterest;
import com.livingprogress.mentorme.entities.ProgramCategory;
import com.livingprogress.mentorme.entities.State;
import com.livingprogress.mentorme.entities.UserRole;
import com.livingprogress.mentorme.exceptions.ConfigurationException;
import com.livingprogress.mentorme.exceptions.MentorMeException;
import com.livingprogress.mentorme.services.LookupService;
import com.livingprogress.mentorme.utils.Helper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Spring Data JPA implementation of LookupService. Is effectively thread safe.
 */
@Service
@NoArgsConstructor
public class LookupServiceImpl implements LookupService {
    /**
     * The country repository for Country CRUD operations. Should be non-null after injection.
     */
    @Autowired
    private CountryRepository countryRepository;

    /**
     * The state repository for State CRUD operations. Should be non-null after injection.
     */
    @Autowired
    private StateRepository stateRepository;

    /**
     * The repository for ProfessionalConsultantArea CRUD operations. Should be non-null after injection.
     */
    @Autowired
    private ProfessionalConsultantAreaRepository professionalConsultantAreaRepository;

    /**
     * The goal category repository for GoalCategory CRUD operations. Should be non-null after injection.
     */
    @Autowired
    private GoalCategoryRepository goalCategoryRepository;

    /**
     * The program category repository for ProgramCategory CRUD operations. Should be non-null after injection.
     */
    @Autowired
    private ProgramCategoryRepository programCategoryRepository;

    /**
     * The personal interest repository for PersonalInterest CRUD operations. Should be non-null after injection.
     */
    @Autowired
    private PersonalInterestRepository personalInterestRepository;

    /**
     * The professional interest repository for ProfessionalInterest CRUD operations. Should be non-null after
     * injection.
     */
    @Autowired
    private ProfessionalInterestRepository professionalInterestRepository;

    /**
     * The document category repository for DocumentCategory CRUD operations. Should be non-null after injection.
     */
    @Autowired
    private DocumentCategoryRepository documentCategoryRepository;

    /**
     * The user role repository for UserRole CRUD operations. Should be non-null after injection.
     */
    @Autowired
    private UserRoleRepository userRoleRepository;

    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        Helper.checkConfigNotNull(countryRepository, "countryRepository");
        Helper.checkConfigNotNull(stateRepository, "stateRepository");
        Helper.checkConfigNotNull(professionalConsultantAreaRepository, "professionalConsultantAreaRepository");
        Helper.checkConfigNotNull(goalCategoryRepository, "goalCategoryRepository");
        Helper.checkConfigNotNull(programCategoryRepository, "programCategoryRepository");
        Helper.checkConfigNotNull(personalInterestRepository, "personalInterestRepository");
        Helper.checkConfigNotNull(professionalInterestRepository, "professionalInterestRepository");
        Helper.checkConfigNotNull(documentCategoryRepository, "documentCategoryRepository");
        Helper.checkConfigNotNull(userRoleRepository, "userRoleRepository");
    }

    /**
     * This method is used to get user role lookups.
     *
     * @return the lookups for user role.
     * @throws MentorMeException if any other error occurred during operation
     */
    public List<UserRole> getUserRoles() throws MentorMeException {
        return userRoleRepository.findAll();
    }

    /**
     * This method is used to get country lookups.
     *
     * @return the lookups for country.
     * @throws MentorMeException if any other error occurred during operation
     */
    public List<Country> getCountries() throws MentorMeException {
        return countryRepository.findAll();
    }

    /**
     * This method is used to get state lookups.
     *
     * @return the lookups for state.
     * @throws MentorMeException if any other error occurred during operation
     */
    public List<State> getStates() throws MentorMeException {
        return stateRepository.findAll();
    }

    /**
     * This method is used to get professional consultant area lookups.
     *
     * @return the lookups for professional consultant area.
     * @throws MentorMeException if any other error occurred during operation
     */
    public List<ProfessionalConsultantArea> getProfessionalConsultantAreas() throws MentorMeException {
        return professionalConsultantAreaRepository.findAll();
    }

    /**
     * This method is used to get goal category lookups.
     *
     * @return the lookups for goal category
     * @throws MentorMeException if any other error occurred during operation
     */
    public List<GoalCategory> getGoalCategories() throws MentorMeException {
        return goalCategoryRepository.findAll();
    }

    /**
     * This method is used to program category lookups.
     *
     * @return the lookups for program category
     * @throws MentorMeException if any other error occurred during operation
     */
    public List<ProgramCategory> getProgramCategories() throws MentorMeException {
        return programCategoryRepository.findAll();
    }

    /**
     * This method is used to personal interest lookups.
     *
     * @return the lookups for personal interest
     * @throws MentorMeException if any other error occurred during operation
     */
    public List<PersonalInterest> getPersonalInterests() throws MentorMeException {
        return personalInterestRepository.findAll();
    }

    /**
     * This method is used to professional interest lookups.
     *
     * @return the lookups for professional interest
     * @throws MentorMeException if any other error occurred during operation
     */
    public List<ProfessionalInterest> getProfessionalInterests() throws MentorMeException {
        return professionalInterestRepository.findAll();
    }

    /**
     * This method is used to document category lookups.
     *
     * @return the lookups for document category
     * @throws MentorMeException if any other error occurred during operation
     */
    public List<DocumentCategory> getDocumentCategories() throws MentorMeException {
        return documentCategoryRepository.findAll();
    }
}

