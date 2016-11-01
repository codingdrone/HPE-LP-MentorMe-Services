package com.livingprogress.mentorme.services.springdata;

import com.livingprogress.mentorme.entities.InstitutionAffiliationCode;
import com.livingprogress.mentorme.entities.InstitutionAffiliationCodeSearchCriteria;
import com.livingprogress.mentorme.exceptions.MentorMeException;
import com.livingprogress.mentorme.services.InstitutionAffiliationCodeService;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * The Spring Data JPA implementation of InstitutionAffiliationCodeService, extends BaseService<InstitutionAffiliationCode,InstitutionAffiliationCodeSearchCriteria>. Effectively thread safe after configuration.
 */
@Service
@NoArgsConstructor
public class InstitutionAffiliationCodeServiceImpl extends BaseService<InstitutionAffiliationCode, InstitutionAffiliationCodeSearchCriteria>
        implements InstitutionAffiliationCodeService {

    /**
     * This method is used to get the specification.
     *
     * @param criteria the search criteria
     * @return the specification
     * @throws MentorMeException if any other error occurred during operation
     */
    protected Specification<InstitutionAffiliationCode>
            getSpecification(InstitutionAffiliationCodeSearchCriteria criteria) throws MentorMeException {
        return new InstitutionAffiliationCodeSpecification(criteria);
    }
}

