package io.github.nnolab.util.taskexe.impl.executors.shells;

import io.github.nnolab.util.context.Context;
import io.github.nnolab.util.taskexe.*;

import java.util.concurrent.TimeUnit;

/**
 * A {@link TaskExecutorService} implementation that encapsulates instance
 * of another implementation, hiding protected and default-access members.
 *
 * @author nnolab
 */
public class TaskExecutorServiceShell implements TaskExecutorService {

    private final TaskExecutorService original;

    /**
     * Constructor with original instance.
     *
     * @param original original instance
     */
    public TaskExecutorServiceShell(TaskExecutorService original) {
        this.original = original;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskControl execute(Task task, Context context) {
        return original.execute(task, context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTaskCount() {
        return original.getTaskCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tasks getTasks() {
        return original.getTasks();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdown() {
        original.shutdown();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdownNow() {
        original.shutdownNow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTerminating() {
        return original.isTerminating();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTerminated() {
        return original.isTerminated();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void awaitTermination() throws InterruptedException {
        original.awaitTermination();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean awaitTermination(long timeout) throws InterruptedException {
        return original.awaitTermination(timeout);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return original.awaitTermination(timeout, unit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object awaitAction(Object statePoint) throws InterruptedException {
        return original.awaitAction(statePoint);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object awaitAction(Object statePoint, long timeout) throws InterruptedException {
        return original.awaitAction(statePoint, timeout);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object awaitAction(Object statePoint, long timeout, TimeUnit unit) throws InterruptedException {
        return original.awaitAction(statePoint, timeout, unit);
    }
}
