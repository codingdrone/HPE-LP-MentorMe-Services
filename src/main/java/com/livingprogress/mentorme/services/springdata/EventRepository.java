package com.livingprogress.mentorme.services.springdata;

import com.livingprogress.mentorme.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * The Event repository.
 */
public interface EventRepository extends JpaRepository<Event,Long>, JpaSpecificationExecutor<Event> {
}

