package com.livingprogress.mentorme.services.springdata;

import com.livingprogress.mentorme.utils.Helper;
import com.livingprogress.mentorme.entities.Mentor;
import com.livingprogress.mentorme.entities.MentorSearchCriteria;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * The specification used to query Mentor by criteria.
 */
@AllArgsConstructor
public class MentorSpecification implements Specification<Mentor> {
    /**
     * The criteria. Final.
     */
    private final MentorSearchCriteria criteria;


    /**
     * Creates a WHERE clause for a query of the referenced entity in form of a Predicate for the given Root and CriteriaQuery.
     *
     * @param root the root
     * @param query the criteria query
     * @param cb the query builder
     * @return the predicate
     */
    public Predicate toPredicate(Root<Mentor> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        query.distinct(true);
        Predicate pd = cb.and();
        pd = Helper.buildPredicate(criteria, pd, root, cb);
        pd = Helper.buildEqualPredicate(criteria.getMentorType(), pd, root.get("mentorType"), cb);
        pd = Helper.buildInPredicate(criteria.getProfessionalAreas(), pd, root.join("professionalAreas", JoinType.LEFT).get("id"), cb);
        pd = Helper.buildEqualPredicate(criteria.getMentorRequestStatus(), pd, root.join("menteeMentorPrograms", JoinType.LEFT).get("requestStatus"), cb);
        pd = Helper.buildLikePredicate(criteria.getCompanyName(), pd, root.get("companyName"), cb);
        return pd;
    }
}

