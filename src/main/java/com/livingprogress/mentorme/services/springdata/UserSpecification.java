package com.livingprogress.mentorme.services.springdata;

import com.livingprogress.mentorme.utils.Helper;
import com.livingprogress.mentorme.entities.User;
import com.livingprogress.mentorme.entities.UserSearchCriteria;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * The specification used to query User by criteria.
 */
@AllArgsConstructor
public class UserSpecification implements Specification<User> {
    /**
     * The criteria. Final.
     */
    private final UserSearchCriteria criteria;


    /**
     * Creates a WHERE clause for a query of the referenced entity in form of a Predicate for the given Root and CriteriaQuery.
     * @param root the root
     * @param query the criteria query
     * @param cb the query builder
     * @return the predicate
     */
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate pd = cb.and();
        pd = Helper.buildLikePredicate(criteria.getEmail(), pd, root.get("email"), cb);
        pd = Helper.buildNamePredicate(criteria.getName(),pd, root, cb);
        pd = Helper.buildLikePredicate(criteria.getUsername(), pd, root.get("username"), cb);
        if (criteria.getRole() != null) {
            pd = cb.and(pd, cb.equal(root.join("roles").get("id"),
                    criteria.getRole().getId()));
        }
        return pd;
    }
}

