package com.livingprogress.mentorme.services.springdata;

import com.livingprogress.mentorme.entities.InstitutionalProgram;
import com.livingprogress.mentorme.entities.InstitutionalProgramSearchCriteria;
import com.livingprogress.mentorme.exceptions.MentorMeException;
import com.livingprogress.mentorme.services.InstitutionalProgramService;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * The Spring Data JPA implementation of InstitutionalProgramService, extends BaseService<InstitutionalProgram,InstitutionalProgramSearchCriteria>. Effectively thread safe after configuration.
 */
@Service
@NoArgsConstructor
public class InstitutionalProgramServiceImpl extends BaseService<InstitutionalProgram, InstitutionalProgramSearchCriteria> implements InstitutionalProgramService {

    /**
     * This method is used to get the specification.
     *
     * @param criteria the search criteria
     * @return the specification
     * @throws MentorMeException if any other error occurred during operation
     */
    protected Specification<InstitutionalProgram> getSpecification(InstitutionalProgramSearchCriteria criteria) throws MentorMeException {
        return new InstitutionalProgramSpecification(criteria);
    }
}

