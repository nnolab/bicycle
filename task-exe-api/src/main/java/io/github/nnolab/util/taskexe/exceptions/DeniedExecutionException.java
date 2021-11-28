package io.github.nnolab.util.taskexe.exceptions;

import io.github.nnolab.util.taskexe.TaskExecutor;

/**
 * Exception thrown by {@link TaskExecutor}, when task cannot be accepted for execution.
 *
 * @author nnolab
 */
public class DeniedExecutionException extends RuntimeException {

    private static final long serialVersionUID = 4097917874417777384L;

    /**
     * Default constructor.
     */
    public DeniedExecutionException() {
    }

    /**
     * Constructor with message.
     *
     * @param message message
     */
    public DeniedExecutionException(String message) {
        super(message);
    }

    /**
     * Constructor with cause.
     *
     * @param cause cause
     */
    public DeniedExecutionException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor with message and cause.
     *
     * @param message message
     * @param cause cause
     */
    public DeniedExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
