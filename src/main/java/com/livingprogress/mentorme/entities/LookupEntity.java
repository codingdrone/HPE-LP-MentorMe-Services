package com.livingprogress.mentorme.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.MappedSuperclass;

/**
 * Base lookup entity.
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
public abstract class LookupEntity extends IdentifiableEntity {
    /**
     * The lookup value.
     */
    private String value;
}

