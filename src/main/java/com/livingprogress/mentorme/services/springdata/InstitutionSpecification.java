package com.livingprogress.mentorme.services.springdata;

import com.livingprogress.mentorme.utils.Helper;
import com.livingprogress.mentorme.entities.Institution;
import com.livingprogress.mentorme.entities.InstitutionSearchCriteria;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * The specification used to query Institution by criteria.
 */
@AllArgsConstructor
public class InstitutionSpecification implements Specification<Institution> {
    /**
     * The criteria. Final.
     */
    private final InstitutionSearchCriteria criteria;


    /**
     * Creates a WHERE clause for a query of the referenced entity in form of a Predicate for the given Root and CriteriaQuery.
     *
     * @param root the root
     * @param query the criteria query
     * @param cb the query builder
     * @return the predicate
     */
    public Predicate toPredicate(Root<Institution> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate pd = cb.and();
        pd = Helper.buildLikePredicate(criteria.getInstitutionName(), pd, root.get("institutionName"), cb);
        pd = Helper.buildLikePredicate(criteria.getParentOrganization(), pd, root.get("parentOrganization"), cb);
        if (!Helper.isNullOrEmpty(criteria.getLocation())) {
            pd = cb.and(pd, cb.or(Helper.buildLike(criteria.getLocation(), root.get("streetAddress"), cb),
                Helper.buildLike(criteria.getLocation(), root.get("city"), cb)));
        }
        pd = Helper.buildLikePredicate(criteria.getEmail(), pd, root.get("email"), cb);
        pd = Helper.buildLikePredicate(criteria.getDescription(), pd, root.get("description"), cb);
        pd = Helper.buildEqualPredicate(criteria.getStatus(), pd, root.get("status"), cb);
        return pd;
    }
}

