package com.livingprogress.mentorme.exceptions;

/**
 * The configuration exception for the application. Thrown if there is an error during in configuration.
 */
public class ConfigurationException extends RuntimeException {

    /**
     * <p>
     * This is the constructor of <code>ConfigurationException</code> class with message argument.
     * </p>
     *
     * @param message the error message.
     */
    public ConfigurationException(String message) {
        super(message);
    }

    /**
     * <p>
     * This is the constructor of <code>ConfigurationException</code> class with message and cause arguments.
     * </p>
     *
     * @param message the error message.
     * @param cause the cause of the exception.
     */
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}

