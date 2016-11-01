package com.livingprogress.mentorme.services.springdata;

import com.livingprogress.mentorme.entities.GoalCategory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The GoalCategory repository.
 */
public interface GoalCategoryRepository extends JpaRepository<GoalCategory,Long> {
}

