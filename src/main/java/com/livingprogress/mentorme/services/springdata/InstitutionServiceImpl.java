package com.livingprogress.mentorme.services.springdata;

import com.livingprogress.mentorme.entities.Institution;
import com.livingprogress.mentorme.entities.InstitutionSearchCriteria;
import com.livingprogress.mentorme.exceptions.MentorMeException;
import com.livingprogress.mentorme.services.InstitutionService;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * The Spring Data JPA implementation of InstitutionService, extends BaseService<Institution,InstitutionSearchCriteria>. Effectively thread safe after configuration.
 */
@Service
@NoArgsConstructor
public class InstitutionServiceImpl extends BaseService<Institution, InstitutionSearchCriteria> implements InstitutionService {

    /**
     * This method is used to get the specification.
     *
     * @param criteria: the search criteria
     * @return the specification
     * @throws MentorMeException if any other error occurred during operation
     */
    protected Specification<Institution> getSpecification(InstitutionSearchCriteria criteria) throws MentorMeException {
        return new InstitutionSpecification(criteria);
    }


    /**
     * This method is used to handle nested properties.
     *
     * @param entity the entity
     * @throws MentorMeException if any error occurred during operation
     */
    @Override
    protected void handleNestedProperties(Institution entity) throws MentorMeException {
        super.handleNestedProperties(entity);
        if (entity.getContacts() != null) {
            entity.getContacts().forEach(c -> c.setInstitution(entity));
        }
    }
}

