package com.livingprogress.mentorme.services.springdata;

import com.livingprogress.mentorme.utils.Helper;
import com.livingprogress.mentorme.entities.Mentee;
import com.livingprogress.mentorme.entities.MenteeSearchCriteria;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * The specification used to query Mentee by criteria.
 */
@AllArgsConstructor
public class MenteeSpecification implements Specification<Mentee>{
    /**
     * The criteria. Final.
     */
    private final MenteeSearchCriteria criteria;

    /**
     * Creates a WHERE clause for a query of the referenced entity in form of a Predicate for the given Root and CriteriaQuery.
     * @param root the root
     * @param query the criteria query
     * @param cb the query builder
     * @return the predicate
     */
    public Predicate toPredicate(Root<Mentee> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        query.distinct(true);
        Predicate pd = cb.and();
        pd = Helper.buildPredicate(criteria, pd, root, cb);
        pd = Helper.buildLikePredicate(criteria.getSchoolName(), pd, root.get("school"), cb);
        return pd;
    }
}

