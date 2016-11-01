package com.livingprogress.mentorme.services.springdata;

import com.livingprogress.mentorme.entities.Event;
import com.livingprogress.mentorme.entities.EventSearchCriteria;
import com.livingprogress.mentorme.exceptions.MentorMeException;
import com.livingprogress.mentorme.services.EventService;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * The Spring Data JPA implementation of EventService, extends BaseService<Event,EventSearchCriteria>. Effectively thread safe after configuration.
 */
@Service
@NoArgsConstructor
public class EventServiceImpl extends BaseService<Event, EventSearchCriteria> implements EventService {

    /**
     * This method is used to get the specification.
     *
     * @param criteria the search criteria
     * @return the specification
     * @throws MentorMeException if any other error occurred during operation
     */
    protected Specification<Event> getSpecification(EventSearchCriteria criteria) throws MentorMeException {
        return new EventSpecification(criteria);
    }

}

