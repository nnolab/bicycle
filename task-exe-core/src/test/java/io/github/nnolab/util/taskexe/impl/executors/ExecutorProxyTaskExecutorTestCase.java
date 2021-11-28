package io.github.nnolab.util.taskexe.impl.executors;

import java.util.Objects;
import java.util.concurrent.*;

/**
 * Tests for {@link ExecutorProxyTaskExecutor}.
 *
 * @author nnolab
 */
public class ExecutorProxyTaskExecutorTestCase
        extends AbstractTaskExecutorTestCase<ExecutorProxyTaskExecutor> {

    protected final ExecutorService executorService = Executors.newSingleThreadExecutor();

    /**
     * {@inheritDoc}
     */
    @Override
    protected ExecutorProxyTaskExecutor getSynchronousTaskExecutor() {
        return new ExecutorProxyTaskExecutor(Runnable::run);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ExecutorProxyTaskExecutor getAsynchronousSingleThreadTaskExecutor() {
        return new ExecutorProxyTaskExecutor(executorService);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ExecutorProxyTaskExecutor getAsynchronousSingleThreadTaskExecutor(ThreadFactory threadFactory) {
        return new ExecutorProxyTaskExecutor(Executors.newSingleThreadExecutor(Objects.requireNonNull(threadFactory)));
    }
}
