package com.livingprogress.mentorme.entities;

import lombok.Getter;
import lombok.Setter;


/**
 * The paging parameters.
 */
@Getter
@Setter
public class Paging {
    /**
     * The page number.
     */
    private int pageNumber;

    /**
     * The page size.
     */
    private int pageSize;

    /**
     * The sort column.
     */
    private String sortColumn;

    /**
     * The sort order.
     */
    private SortOrder sortOrder;
}

