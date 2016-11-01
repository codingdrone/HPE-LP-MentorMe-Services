package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import java.util.List;

/**
 * The task.
 */
@Getter
@Setter
@Entity
public class Task extends IdentifiableEntity {
    /**
     * The description.
     */
    private String description;

    /**
     * The duration in days.
     */
    private int durationInDays;

    /**
     * The mentor assignment.
     */
    private boolean mentorAssignment;

    /**
     * The mentee assignment.
     */
    private boolean menteeAssignment;

    /**
     * The documents.
     */
    @ManyToMany
    @JoinTable(name = "task_document", joinColumns = {@JoinColumn(name = "task_id")}, inverseJoinColumns = {@JoinColumn(name = "document_id")})
    private List<Document> documents;

    /**
     * The useful links.
     */
    @ManyToMany
    @JoinTable(name = "task_useful_link", joinColumns = {@JoinColumn(name = "task_id")}, inverseJoinColumns = {@JoinColumn(name = "useful_link_id")})
    private List<UsefulLink> usefulLinks;

    /**
     * Flag for the custom task (the custom goal created for the mentee-mentor pair).
     */
    private boolean custom;

    /**
     * Custom assigned task data.
     */
    @OneToOne(mappedBy = "task")
    private CustomAssignedTaskData customData;

    /**
     * The goal id
     */
    @JoinColumn(name = "goal_id")
    private long goalId;
}

