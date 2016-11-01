package com.livingprogress.mentorme.services.springdata;

import com.livingprogress.mentorme.exceptions.EntityNotFoundException;
import com.livingprogress.mentorme.utils.Helper;
import com.livingprogress.mentorme.entities.Mentee;
import com.livingprogress.mentorme.entities.MenteeSearchCriteria;
import com.livingprogress.mentorme.entities.UserStatus;
import com.livingprogress.mentorme.exceptions.ConfigurationException;
import com.livingprogress.mentorme.exceptions.MentorMeException;
import com.livingprogress.mentorme.services.MenteeService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.List;

/**
 * The Spring Data JPA implementation of MenteeService.
 */
@Service
@NoArgsConstructor
public class MenteeServiceImpl extends BaseService<Mentee, MenteeSearchCriteria> implements MenteeService {
    /**
     * The mentee mentor program repository for CRUD operations. Should be non-null after injection.
     */
    @Autowired
    private MenteeMentorProgramRepository menteeMentorProgramRepository;

    /**
     * The mentee repository for CRUD operations. Should be non-null after injection.
     */
    @Autowired
    private MenteeRepository menteeRepository;

    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        super.checkConfiguration();
        Helper.checkConfigNotNull(menteeMentorProgramRepository, "menteeMentorProgramRepository");
        Helper.checkConfigNotNull(menteeRepository, "menteeRepository");
    }

    /**
     * This method is used to get the specification.
     *
     * @param criteria the search criteria
     * @return the specification
     * @throws MentorMeException if any other error occurred during operation
     */
    protected Specification<Mentee> getSpecification(MenteeSearchCriteria criteria) throws MentorMeException {
        return new MenteeSpecification(criteria);
    }

    /**
     * This method is used to handle nested properties.
     *
     * @param entity the entity
     * @throws MentorMeException if any error occurred during operation
     */
    @Override
    protected void handleNestedProperties(Mentee entity) throws MentorMeException {
        super.handleNestedProperties(entity);
        super.handleInstitutionUserNestedProperties(entity);
    }

    /**
     * This method is used to get the program mentees.
     *
     * @param programId the mentee mentor program id.
     * @return  the match program mentees.
     * @throws IllegalArgumentException if programId is not positive
     * @throws MentorMeException if any other error occurred during operation
     */
    public List<Mentee> getProgramMentees(long programId) throws MentorMeException {
        Helper.checkPositive(programId, "programId");
        return menteeMentorProgramRepository.getProgramMentees(programId);
    }

    /**
     * This method is used to get the mentee avg score.
     *
     * @param id the id of the entity to retrieve
     * @return the avg score
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws MentorMeException if any other error occurred during operation
     */
    public int getAverageMenteeScore(long id) throws MentorMeException {
        get(id);
        return menteeMentorProgramRepository.getAverageMenteeScore(id);
    }

    /**
     * This method is used to create an entity.
     *
     * @param entity the entity to create
     * @return the created entity
     * @throws IllegalArgumentException if entity is null or not valid
     * @throws MentorMeException if any other error occurred during operation
     */
    @Transactional
    public Mentee create(Mentee entity) throws MentorMeException {
        Helper.encodePassword(entity, false);
        return super.create(entity);
    }

    /**
     * This method is used to update an entity.
     *
     * @param id the id of the entity to update
     * @param entity the entity to update
     * @return the updated entity
     * @throws IllegalArgumentException if id is not positive or entity is null or id of entity is not positive
     * or id of  entity not match id or entity is invalid
     * @throws EntityNotFoundException if the entity does not exist
     * @throws MentorMeException if any other error occurred during operation
     */
    @Transactional
    public Mentee update(long id, Mentee entity) throws MentorMeException {
        Mentee existing = super.checkUpdate(id, entity);
        if (Helper.isUpdated(existing, entity)) {
            return getRepository().save(existing);
        }
        return existing;
    }

    /**
     * This method is used to confirm the parent consent.
     *
     * @param token the token
     * @return true if found parent consent for inactive mentee
     * @throws IllegalArgumentException if token is null or empty
     * @throws MentorMeException if any other error occurred during operation
     */
    @Transactional
    public boolean confirmParentConsent(String token) throws MentorMeException {
        Mentee mentee = menteeRepository.findByParentConsentToken(token);
        if (mentee != null && !UserStatus.ACTIVE.equals(mentee.getStatus())) {
            mentee.setStatus(UserStatus.ACTIVE);
            menteeRepository.save(mentee);
            return true;
        }
        return false;
    }
}

