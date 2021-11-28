package io.github.nnolab.util.taskexe.impl.executors;

import java.util.concurrent.Executor;
import java.util.function.Function;

/**
 * Tests for {@link SeparatedTaskExecutorService}.
 *
 * @author nnolab
 */
public class SeparatedTaskExecutorServiceTestCase
        extends AbstractExecutorProxyServiceTestCase<SeparatedTaskExecutorService> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected Function<Executor, SeparatedTaskExecutorService> getConstructor() {
        return SeparatedTaskExecutorService::new;
    }
}
