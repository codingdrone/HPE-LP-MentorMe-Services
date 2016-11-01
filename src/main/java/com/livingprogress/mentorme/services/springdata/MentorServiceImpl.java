package com.livingprogress.mentorme.services.springdata;

import com.livingprogress.mentorme.exceptions.EntityNotFoundException;
import com.livingprogress.mentorme.utils.Helper;
import com.livingprogress.mentorme.entities.Mentor;
import com.livingprogress.mentorme.entities.MentorSearchCriteria;
import com.livingprogress.mentorme.exceptions.ConfigurationException;
import com.livingprogress.mentorme.exceptions.MentorMeException;
import com.livingprogress.mentorme.services.MentorService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.List;

/**
 * The Spring Data JPA implementation of MentorService.
 */
@Service
@NoArgsConstructor
public class MentorServiceImpl extends BaseService<Mentor, MentorSearchCriteria> implements MentorService {
    /**
     * The mentee mentor program repository for CRUD operations. Should be non-null after injection.
     */
    @Autowired
    private MenteeMentorProgramRepository menteeMentorProgramRepository;

    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        super.checkConfiguration();
        Helper.checkConfigNotNull(menteeMentorProgramRepository, "menteeMentorProgramRepository");
    }

    /**
     * This method is used to get the specification.
     *
     * @param criteria the search criteria
     * @return the specification
     * @throws MentorMeException if any other error occurred during operation
     */
    protected Specification<Mentor> getSpecification(MentorSearchCriteria criteria) throws MentorMeException {
        return new MentorSpecification(criteria);
    }

    /**
     * This method is used to handle nested properties.
     *
     * @param entity the entity
     * @throws MentorMeException if any error occurred during operation
     */
    @Override
    protected void handleNestedProperties(Mentor entity) throws MentorMeException {
        super.handleNestedProperties(entity);
        super.handleInstitutionUserNestedProperties(entity);
        if (entity.getProfessionalExperiences() != null) {
            entity.getProfessionalExperiences().forEach(p -> p.setMentor(entity));
        }
    }

    /**
     * This method is used to get the program mentors.
     *
     * @param programId the mentee mentor program id.
     * @return the match program mentors.
     * @throws IllegalArgumentException if programId is not positive
     * @throws MentorMeException if any other error occurred during operation
     */
    public List<Mentor> getProgramMentors(long programId) throws MentorMeException {
        Helper.checkPositive(programId, "programId");
        return menteeMentorProgramRepository.getProgramMentors(programId);
    }

    /**
     * This method is used to get the mentor avg score.
     *
     * @param id the id of the entity to retrieve
     * @return the avg score
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws MentorMeException if any other error occurred during operation
     */
    public int getAverageMentorScore(long id) throws MentorMeException {
        get(id);
        return menteeMentorProgramRepository.getAverageMentorScore(id);
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
    public Mentor create(Mentor entity) throws MentorMeException {
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
    public Mentor update(long id, Mentor entity) throws MentorMeException {
        Mentor existing = super.checkUpdate(id, entity);
        if (Helper.isUpdated(existing, entity)) {
            return getRepository().save(existing);
        }
        return existing;
    }
}

