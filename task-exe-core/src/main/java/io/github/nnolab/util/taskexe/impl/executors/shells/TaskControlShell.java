package io.github.nnolab.util.taskexe.impl.executors.shells;

import io.github.nnolab.util.context.Context;
import io.github.nnolab.util.taskexe.*;
import io.github.nnolab.util.taskexe.exceptions.UselessWaitingException;

import java.util.concurrent.TimeUnit;

/**
 * A {@link TaskControl} implementation that encapsulates instance
 * of another implementation, hiding protected and default-access members.
 *
 * @author nnolab
 */
public class TaskControlShell implements TaskControl {

    private final TaskControl original;

    /**
     * Constructor with original instance.
     *
     * @param original original instance
     */
    public TaskControlShell(TaskControl original) {
        this.original = original;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task getTask() {
        return original.getTask();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Context getContext() {
        return original.getContext();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskExecutor getExecutor() {
        return original.getExecutor();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskStage getTaskStage() {
        return original.getTaskStage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Throwable getFailureCause() {
        return original.getFailureCause();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskStage awaitNextStage(TaskStage startStage)
            throws UselessWaitingException, InterruptedException {
        return original.awaitNextStage(startStage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskStage awaitNextStage(TaskStage startStage, long timeout)
            throws UselessWaitingException, InterruptedException {
        return original.awaitNextStage(startStage, timeout);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskStage awaitNextStage(TaskStage startStage, long timeout, TimeUnit unit) throws UselessWaitingException, InterruptedException {
        return original.awaitNextStage(startStage, timeout, unit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cancelTask() {
        original.cancelTask();
    }
}
