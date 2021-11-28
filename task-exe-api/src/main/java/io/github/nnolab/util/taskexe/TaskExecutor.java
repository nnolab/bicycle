package io.github.nnolab.util.taskexe;

import io.github.nnolab.util.context.Context;
import io.github.nnolab.util.taskexe.exceptions.DeniedExecutionException;

/**
 * Interface, that provides execution of given task with given context
 * in implementation-dependent mode with control over execution.
 *
 * @author nnolab
 */
@FunctionalInterface
public interface TaskExecutor {

    /**
     * Submit specified task for execution with specified context.
     * Context may be {@code null}.
     * Returned {@link TaskControl} enables
     * monitoring and control over task execution.
     *
     * @param task task to be executed
     * @param context context for task
     * @return control over task execution
     * @throws NullPointerException if specified task is {@code null}
     * @throws DeniedExecutionException if executor denied to execute task
     */
    TaskControl execute(Task task, Context context);
}
