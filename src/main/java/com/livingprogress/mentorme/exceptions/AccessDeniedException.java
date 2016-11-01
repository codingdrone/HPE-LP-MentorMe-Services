package com.livingprogress.mentorme.exceptions;

/**
 * This is exception is thrown if a principal does not have access to a specific entity.
 */
public class AccessDeniedException extends MentorMeException {

    /**
     * <p>
     * This is the constructor of <code>AccessDeniedException</code> class with message argument.
     * </p>
     *
     * @param message the error message.
     */
    public AccessDeniedException(String message) {
        super(message);
    }

    /**
     * <p>
     * This is the constructor of <code>AccessDeniedException</code> class with message and cause arguments.
     * </p>
     *
     * @param message the error message.
     * @param cause the cause of the exception.
     */
    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}

