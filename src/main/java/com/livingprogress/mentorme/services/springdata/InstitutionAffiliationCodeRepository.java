package com.livingprogress.mentorme.services.springdata;

import com.livingprogress.mentorme.entities.InstitutionAffiliationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * The InstitutionAffiliationCode repository。
 */
public interface InstitutionAffiliationCodeRepository extends JpaRepository<InstitutionAffiliationCode, Long>,
        JpaSpecificationExecutor<InstitutionAffiliationCode> {

}

