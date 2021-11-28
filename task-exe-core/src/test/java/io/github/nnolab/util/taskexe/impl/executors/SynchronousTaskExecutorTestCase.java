package io.github.nnolab.util.taskexe.impl.executors;

import io.github.nnolab.util.taskexe.*;
import java.util.concurrent.ThreadFactory;

import static io.github.nnolab.util.taskexe.TaskControl.TaskStage.*;
import static org.junit.Assert.*;

/**
 * Tests for {@link SynchronousTaskExecutor}.
 *
 * @author nnolab
 */
public class SynchronousTaskExecutorTestCase
        extends AbstractTaskExecutorTestCase<SynchronousTaskExecutor> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected SynchronousTaskExecutor getSynchronousTaskExecutor() {
        return new SynchronousTaskExecutor();
    }

    /**
     * Not supported.
     */
    @Override
    protected SynchronousTaskExecutor getAsynchronousSingleThreadTaskExecutor() {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     */
    @Override
    protected SynchronousTaskExecutor getAsynchronousSingleThreadTaskExecutor(ThreadFactory threadFactory) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testAsynchronousMode() {
        //not supported
    }

    @Override
    public void testTaskStateWaiting() {
        //not supported
    }

    @Override
    public void testCancelTask() {
        //not supported
    }

    @Override
    public void testThreadDeath() throws Exception {
        SynchronousTaskExecutor taskExecutor = getSynchronousTaskExecutor();
        ThreadDeath threadDeath = new ThreadDeath();
        Task successfulTask = context -> {};
        Task failedTask = context -> {throw threadDeath;};

        for (int i = 0; i < 3; i++) {
            try {
                taskExecutor.execute(failedTask, null);
                fail();
            } catch (ThreadDeath e) {
                assertEquals(threadDeath, e);
            }
            TaskControl taskControl = taskExecutor.execute(successfulTask, null);
            assertEquals(COMPLETE, taskControl.awaitNextStage(RUNNING));
            assertNull(taskControl.getFailureCause());
        }
    }
}
