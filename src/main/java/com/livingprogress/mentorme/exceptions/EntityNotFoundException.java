package com.livingprogress.mentorme.exceptions;

/**
 * This is exception is thrown if there is no entity with given id.
 */
public class EntityNotFoundException extends MentorMeException {

    /**
     * <p>
     * This is the constructor of <code>EntityNotFoundException</code> class with message argument.
     * </p>
     *
     * @param message the error message.
     */
    public EntityNotFoundException(String message) {
        super(message);
    }

    /**
     * <p>
     * This is the constructor of <code>EntityNotFoundException</code> class with message and cause arguments.
     * </p>
     *
     * @param message the error message.
     * @param cause the cause of the exception.
     */
    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

