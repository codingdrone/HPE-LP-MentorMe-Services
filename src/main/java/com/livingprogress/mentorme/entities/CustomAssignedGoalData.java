package com.livingprogress.mentorme.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * The custom assigned goal data.
 */
@Getter
@Setter
@Entity
public class CustomAssignedGoalData extends CustomAssignedData {
    /**
     * The goal.
     */
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "goal_id")
    private Goal goal;
}

