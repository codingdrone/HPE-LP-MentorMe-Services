package com.livingprogress.mentorme.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * The custom assigned task data.
 */
@Getter
@Setter
@Entity
public class CustomAssignedTaskData extends CustomAssignedData {
    /**
     * The task.
     */
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "task_id")
    private Task task;
}

