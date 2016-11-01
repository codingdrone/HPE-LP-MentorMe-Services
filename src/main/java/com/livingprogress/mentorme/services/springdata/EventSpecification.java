package com.livingprogress.mentorme.services.springdata;

import com.livingprogress.mentorme.entities.Event;
import com.livingprogress.mentorme.entities.EventSearchCriteria;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;

/**
 * The specification used to query Event by criteria.
 */
@AllArgsConstructor
public class EventSpecification implements Specification<Event> {
    /**
     * The criteria. Final.
     */
    private final EventSearchCriteria criteria;

    /**
     * Creates a WHERE clause for a query of the referenced entity in form of a Predicate for the given Root and CriteriaQuery.
     * @param root the root
     * @param query the criteria query
     * @param cb the query builder
     * @return the predicate
     */
    public Predicate toPredicate(Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate pd = cb.and();
        //TODO implement other criteria
        if(Boolean.TRUE.equals(criteria.getUpcoming())){
            Date now = new Date();
            pd = cb.and(pd, cb.and(cb.greaterThan(root.get("startTime"), now),
                     cb.greaterThan(root.get("endTime"), now)));
        }
        return pd;
    }
}

