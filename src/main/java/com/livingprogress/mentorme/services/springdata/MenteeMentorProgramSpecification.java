package com.livingprogress.mentorme.services.springdata;

import com.livingprogress.mentorme.entities.MenteeMentorProgram;
import com.livingprogress.mentorme.entities.MenteeMentorProgramSearchCriteria;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * The specification used to query MenteeMentorProgram by criteria.
 */
@AllArgsConstructor
public class MenteeMentorProgramSpecification implements Specification<MenteeMentorProgram> {
    /**
     * The criteria. Final.
     */
    private final MenteeMentorProgramSearchCriteria criteria;


    /**
     * Creates a WHERE clause for a query of the referenced entity in form of a Predicate for the given Root and CriteriaQuery.
     * @param root the root
     * @param query the criteria query
     * @param cb the query builder
     * @return the predicate
     */
    public Predicate toPredicate(Root<MenteeMentorProgram> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate pd = cb.and();
        //TODO implement criteria
        return pd;
    }
}

