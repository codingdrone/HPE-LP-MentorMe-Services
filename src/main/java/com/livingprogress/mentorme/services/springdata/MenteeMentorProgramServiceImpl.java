package com.livingprogress.mentorme.services.springdata;

import com.livingprogress.mentorme.entities.MenteeMentorProgram;
import com.livingprogress.mentorme.entities.MenteeMentorProgramSearchCriteria;
import com.livingprogress.mentorme.exceptions.MentorMeException;
import com.livingprogress.mentorme.services.MenteeMentorProgramService;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * The Spring Data JPA implementation of MenteeMentorProgramService, extends BaseService<MenteeMentorProgram,
 * MenteeMentorProgramSearchCriteria>. Effectively thread safe after configuration.
 */
@Service
@NoArgsConstructor
public class MenteeMentorProgramServiceImpl extends BaseService<MenteeMentorProgram, MenteeMentorProgramSearchCriteria> implements MenteeMentorProgramService {

    /**
     * This method is used to get the specification.
     *
     * @param criteria the search criteria
     * @return the specification
     * @throws MentorMeException if any other error occurred during operation
     */
    protected Specification<MenteeMentorProgram> getSpecification(MenteeMentorProgramSearchCriteria criteria) throws MentorMeException {
        return new MenteeMentorProgramSpecification(criteria);
    }
}

