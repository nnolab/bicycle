package io.github.nnolab.util.taskexe.impl.executors;

import io.github.nnolab.util.taskexe.TaskExecutorService;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.Function;

/**
 * Base class for testing {@link TaskExecutorService}, delegating
 * task execution to {@link ExecutorService}.
 *
 * @param <T> implementation
 * @author nnolab
 */
public abstract class AbstractExecutorProxyServiceTestCase<T extends TaskExecutorService>
        extends AbstractTaskExecutorServiceTestCase<T> {

    protected final ExecutorService executorService = Executors.newSingleThreadExecutor();

    /**
     * Get constructor, accepting executor.
     *
     * @return constructor, accepting executor
     */
    protected abstract Function<Executor, T> getConstructor();

    /**
     * {@inheritDoc}
     */
    @Override
    protected T getSynchronousTaskExecutor() {
        return getConstructor().apply(Runnable::run);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected T getAsynchronousSingleThreadTaskExecutor() {
        return getConstructor().apply(executorService);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected T getAsynchronousSingleThreadTaskExecutor(ThreadFactory threadFactory) {
        return getConstructor().apply(Executors.newSingleThreadExecutor(Objects.requireNonNull(threadFactory)));
    }
}
