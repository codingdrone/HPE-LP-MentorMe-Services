package com.livingprogress.mentorme.exceptions;


/**
 * The base exception for the application. Thrown if there is an error during CRUD operations.
 */
public class MentorMeException extends Exception {

    /**
     * <p>
     * This is the constructor of <code>MentorMeException</code> class with message argument.
     * </p>
     *
     * @param message the error message.
     */
    public MentorMeException(String message) {
        super(message);
    }

    /**
     * <p>
     * This is the constructor of <code>MentorMeException</code> class with message and cause arguments.
     * </p>
     *
     * @param message the error message.
     * @param cause the cause of the exception.
     */
    public MentorMeException(String message, Throwable cause) {
        super(message, cause);
    }
}


