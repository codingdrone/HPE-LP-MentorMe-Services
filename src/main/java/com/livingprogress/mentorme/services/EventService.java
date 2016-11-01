package com.livingprogress.mentorme.services;

import com.livingprogress.mentorme.entities.Event;
import com.livingprogress.mentorme.entities.EventSearchCriteria;

/**
 * The event service. Extends generic service interface.Implementation should be effectively thread-safe.
*/
public interface EventService extends GenericService<Event,EventSearchCriteria> {
}

