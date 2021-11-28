package io.github.nnolab.util.taskexe.impl.executors;

import java.util.concurrent.Executor;
import java.util.function.Function;

/**
 * Tests for {@link LinkedTaskExecutorService}.
 *
 * @author nnolab
 */
public class LinkedTaskExecutorServiceTestCase
        extends AbstractExecutorProxyServiceTestCase<LinkedTaskExecutorService> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected Function<Executor, LinkedTaskExecutorService> getConstructor() {
        return LinkedTaskExecutorService::new;
    }
}
