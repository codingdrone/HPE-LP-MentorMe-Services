package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * The document.
 */
@Getter
@Setter
@Entity
public class Document extends IdentifiableEntity {
    /**
     * The name.
     */
    private String name;

    /**
     * The path to the document.
     */
    private String path;

    /**
     * The document category.
     */
    @ManyToOne
    @JoinColumn(name = "category_id")
    private DocumentCategory category;
}

