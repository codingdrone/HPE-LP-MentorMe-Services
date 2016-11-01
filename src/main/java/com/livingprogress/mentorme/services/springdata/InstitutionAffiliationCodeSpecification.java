package com.livingprogress.mentorme.services.springdata;

import com.livingprogress.mentorme.utils.Helper;
import com.livingprogress.mentorme.entities.InstitutionAffiliationCode;
import com.livingprogress.mentorme.entities.InstitutionAffiliationCodeSearchCriteria;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * The specification used to query InstitutionAffiliactionCode by criteria.
 */
@AllArgsConstructor
public class InstitutionAffiliationCodeSpecification implements Specification<InstitutionAffiliationCode> {
    /**
     * The criteria. Final.
     */
    private final InstitutionAffiliationCodeSearchCriteria criteria;


    /**
     * Creates a WHERE clause for a query of the referenced entity in form of a Predicate for the given Root and CriteriaQuery.
     * @param root the root
     * @param query the criteria query
     * @param cb the query builder
     * @return the predicate
     */
    public Predicate toPredicate(Root<InstitutionAffiliationCode> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate pd = cb.and();
        pd = Helper.buildEqualPredicate(criteria.getCode(), pd, root.get("code"), cb);
        pd = Helper.buildEqualPredicate(criteria.getInstitutionId(), pd, root.get("institutionId"), cb);
        pd = Helper.buildEqualPredicate(criteria.getUsed(), pd, root.get("used"), cb);
        return pd;
    }
}

