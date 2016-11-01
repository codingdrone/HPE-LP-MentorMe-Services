package com.livingprogress.mentorme.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Base identifiable entity.
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
public abstract class IdentifiableEntity {
    /**
     * The id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Override the equals method.
     *
     * @param target the target
     * @return true if two entities have the same ID
     */
    @Override
    public boolean equals(Object target) {
        if (target instanceof IdentifiableEntity) {
            IdentifiableEntity entity = (IdentifiableEntity) target;
            return entity.getId() == this.id;
        }
        return false;
    }

    /**
     * Override the hashCode method.
     *
     * @return the hash code of the entity
     */
    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}

