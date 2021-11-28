package io.github.nnolab.util.taskexe.impl.executors;

import io.github.nnolab.util.context.Context;
import io.github.nnolab.util.taskexe.*;
import io.github.nnolab.util.taskexe.exceptions.UselessWaitingException;
import io.github.nnolab.util.taskexe.impl.tasks.AbstractInterruptableTask;
import io.github.nnolab.util.taskexe.test.ContextTI;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.*;

import static io.github.nnolab.util.taskexe.TaskControl.TaskStage.*;
import static java.util.concurrent.TimeUnit.*;
import static org.junit.Assert.*;

/**
 * Base class for testing {@link TaskExecutor}.
 *
 * @param <T> implementation
 * @author nnolab
 */
public abstract class AbstractTaskExecutorTestCase<T extends TaskExecutor> {

    /**
     * Get default task executor instance with synchronous
     * task execution.
     *
     * @return synchronous task executor
     */
    protected abstract T getSynchronousTaskExecutor();

    /**
     * Get default task executor instance with asynchronous
     * task execution in single thread.
     *
     * @return asynchronous task executor with single thread
     */
    protected abstract T getAsynchronousSingleThreadTaskExecutor();

    /**
     * Get default task executor instance with asynchronous
     * task execution in single thread, created by given
     * thread factory.
     *
     * @param threadFactory thread factory
     * @return asynchronous task executor with single thread
     * @throws NullPointerException if thread factory is {@code null}
     */
    protected abstract T getAsynchronousSingleThreadTaskExecutor(ThreadFactory threadFactory);

    /**
     * Get empty {@link Context} instance for task execution.
     *
     * @return context
     */
    protected Context getContext() {
        return new ContextTI(1);
    }

    @Test
    public void testSynchronousMode() {

        T taskExecutor = getSynchronousTaskExecutor();
        Context context = getContext();
        String key = "key";
        Object value = new Object();
        Throwable failureCause = new Throwable();
        Task successfulTask = c -> context.put(key, value);
        Task failedTask = c -> {
            throw failureCause;
        };
        long millisTimeout = 100;
        long nanosTimeout = MILLISECONDS.toNanos(millisTimeout);
        long secTimeout = 1;
        long thresholdTimeout = 1000;

        Consumer<Function<TaskControl, TaskControl.TaskStage>> successfulExecutionTest = resultWaiting -> {
            for (int i = 0; i < 3; i++) {
                context.clear();
                TaskControl taskControl = taskExecutor.execute(successfulTask, context);
                assertEquals(successfulTask, taskControl.getTask());
                assertEquals(context, taskControl.getContext());
                assertEquals(taskExecutor, taskControl.getExecutor());
                long start = System.currentTimeMillis();
                Assert.assertEquals(TaskControl.TaskStage.COMPLETE, resultWaiting.apply(taskControl));
                assertTrue(System.currentTimeMillis() - start < thresholdTimeout);
                Assert.assertEquals(TaskControl.TaskStage.COMPLETE, taskControl.getTaskStage());
                assertNull(taskControl.getFailureCause());
                assertEquals(value, context.get(key));
            }
        };

        Consumer<Function<TaskControl, TaskControl.TaskStage>> failedExecutionTest = resultWaiting -> {
            for (int i = 0; i < 3; i++) {
                TaskControl taskControl = taskExecutor.execute(failedTask, context);
                assertEquals(failedTask, taskControl.getTask());
                assertEquals(context, taskControl.getContext());
                assertEquals(taskExecutor, taskControl.getExecutor());
                long start = System.currentTimeMillis();
                Assert.assertEquals(FAILED, resultWaiting.apply(taskControl));
                assertTrue(System.currentTimeMillis() - start < thresholdTimeout);
                Assert.assertEquals(FAILED, taskControl.getTaskStage());
                assertEquals(failureCause, taskControl.getFailureCause());
            }
        };

        //-------------------

        successfulExecutionTest.accept(taskControl -> {
            try {
                return taskControl.awaitNextStage(PENDING);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        successfulExecutionTest.accept(taskControl -> {
            try {
                return taskControl.awaitNextStage(PENDING, millisTimeout);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        successfulExecutionTest.accept(taskControl -> {
            try {
                return taskControl.awaitNextStage(PENDING, nanosTimeout, TimeUnit.NANOSECONDS);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        successfulExecutionTest.accept(taskControl -> {
            try {
                return taskControl.awaitNextStage(PENDING, secTimeout, TimeUnit.SECONDS);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        //-------------------

        failedExecutionTest.accept(taskControl -> {
            try {
                return taskControl.awaitNextStage(PENDING);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        failedExecutionTest.accept(taskControl -> {
            try {
                return taskControl.awaitNextStage(PENDING, millisTimeout);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        failedExecutionTest.accept(taskControl -> {
            try {
                return taskControl.awaitNextStage(PENDING, nanosTimeout, TimeUnit.NANOSECONDS);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        failedExecutionTest.accept(taskControl -> {
            try {
                return taskControl.awaitNextStage(PENDING, secTimeout, TimeUnit.SECONDS);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void testAsynchronousMode() throws Exception {

        final T taskExecutor = getAsynchronousSingleThreadTaskExecutor();
        final Context context = getContext();
        final Throwable failureCause = new Throwable();
        final long millisTimeout = 1000;
        final long nanosTimeout = MILLISECONDS.toNanos(millisTimeout);

        final Task successfulTask = c -> {
            Phaser phaser = c.get(Phaser.class);
            phaser.arriveAndAwaitAdvance();
            phaser.arriveAndAwaitAdvance();
        };

        final Task failedTask = c -> {
            Phaser phaser = c.get(Phaser.class);
            phaser.arriveAndAwaitAdvance();
            phaser.arriveAndAwaitAdvance();
            throw failureCause;
        };

        final AtomicReference<TaskControl> taskControlRef = new AtomicReference<>();
        final AtomicReference<Phaser> phaserRef = new AtomicReference<>();

        final Consumer<Task> taskRunning = task -> {

            long start;
            TaskControl taskControl;
            Phaser phaser;

            context.clear();
            phaser = new Phaser(2);
            context.put(phaser);

            //for pending delay in single thread
            taskExecutor.execute(failedTask, context);
            assertEquals(1, phaser.arriveAndAwaitAdvance());

            taskControl = taskExecutor.execute(task, context);
            assertEquals(task, taskControl.getTask());
            assertEquals(context, taskControl.getContext());
            assertEquals(taskExecutor, taskControl.getExecutor());

            try {
                start = System.currentTimeMillis();
                Assert.assertEquals(PENDING, taskControl.awaitNextStage(PENDING, millisTimeout));
                assertTrue(System.currentTimeMillis() - start >= millisTimeout);
                Assert.assertEquals(PENDING, taskControl.getTaskStage());
                assertNull(taskControl.getFailureCause());
                assertEquals(2, phaser.arriveAndAwaitAdvance());

                assertEquals(3, phaser.arriveAndAwaitAdvance());
                start = System.currentTimeMillis();
                Assert.assertEquals(TaskControl.TaskStage.RUNNING, taskControl.awaitNextStage(PENDING, millisTimeout));
                assertTrue(System.currentTimeMillis() - start < millisTimeout);
                Assert.assertEquals(TaskControl.TaskStage.RUNNING, taskControl.getTaskStage());
                assertNull(taskControl.getFailureCause());

                start = System.currentTimeMillis();
                Assert.assertEquals(TaskControl.TaskStage.RUNNING, taskControl.awaitNextStage(TaskControl.TaskStage.RUNNING, nanosTimeout, TimeUnit.NANOSECONDS));
                assertTrue(System.currentTimeMillis() - start >= TimeUnit.NANOSECONDS.toMillis(nanosTimeout));
                Assert.assertEquals(TaskControl.TaskStage.RUNNING, taskControl.getTaskStage());
                assertNull(taskControl.getFailureCause());
            } catch (UselessWaitingException | InterruptedException e) {
                throw new RuntimeException();
            }

            taskControlRef.set(taskControl);
            phaserRef.set(phaser);
        };

        long start;
        TaskControl taskControl;
        Phaser phaser;

        //-------------------

        taskRunning.accept(successfulTask);

        taskControl = taskControlRef.getAndSet(null);
        phaser = phaserRef.getAndSet(null);

        assertEquals(4, phaser.arriveAndAwaitAdvance());
        Assert.assertEquals(TaskControl.TaskStage.COMPLETE, taskControl.awaitNextStage(TaskControl.TaskStage.RUNNING));
        Assert.assertEquals(TaskControl.TaskStage.COMPLETE, taskControl.awaitNextStage(PENDING));
        start = System.currentTimeMillis();
        Assert.assertEquals(TaskControl.TaskStage.COMPLETE, taskControl.awaitNextStage(TaskControl.TaskStage.RUNNING, nanosTimeout, TimeUnit.NANOSECONDS));
        assertTrue(System.currentTimeMillis() - start < TimeUnit.NANOSECONDS.toMillis(nanosTimeout));
        Assert.assertEquals(TaskControl.TaskStage.COMPLETE, taskControl.getTaskStage());
        assertNull(taskControl.getFailureCause());

        //-------------------

        taskRunning.accept(failedTask);

        taskControl = taskControlRef.getAndSet(null);
        phaser = phaserRef.getAndSet(null);

        assertEquals(4, phaser.arriveAndAwaitAdvance());
        Assert.assertEquals(FAILED, taskControl.awaitNextStage(TaskControl.TaskStage.RUNNING));
        Assert.assertEquals(FAILED, taskControl.awaitNextStage(PENDING));
        start = System.currentTimeMillis();
        Assert.assertEquals(FAILED, taskControl.awaitNextStage(TaskControl.TaskStage.RUNNING, nanosTimeout, TimeUnit.NANOSECONDS));
        assertTrue(System.currentTimeMillis() - start < TimeUnit.NANOSECONDS.toMillis(nanosTimeout));
        Assert.assertEquals(FAILED, taskControl.getTaskStage());
        assertEquals(failureCause, taskControl.getFailureCause());
    }

    @Test
    public void testTaskStateWaiting() throws Exception {

        final T taskExecutor = getAsynchronousSingleThreadTaskExecutor();
        final Throwable failureCause = new Throwable();
        final long pause = 100;

        final Task successfulTask = context -> {
            Thread.sleep(pause);
        };

        final Task failedTask = context -> {
            Thread.sleep(pause);
            throw failureCause;
        };

        TaskControl taskControl;

        taskExecutor.execute(failedTask, null);
        taskControl = taskExecutor.execute(successfulTask, null);
        Assert.assertEquals(TaskControl.TaskStage.RUNNING, taskControl.awaitNextStage(PENDING));
        Assert.assertEquals(TaskControl.TaskStage.COMPLETE, taskControl.awaitNextStage(TaskControl.TaskStage.RUNNING, pause * 2));
        assertNull(taskControl.getFailureCause());

        taskExecutor.execute(failedTask, null);
        taskControl = taskExecutor.execute(failedTask, null);
        Assert.assertEquals(TaskControl.TaskStage.RUNNING, taskControl.awaitNextStage(
                PENDING, MILLISECONDS.toNanos(pause * 2), TimeUnit.NANOSECONDS));
        Assert.assertEquals(FAILED, taskControl.awaitNextStage(TaskControl.TaskStage.RUNNING));
        assertEquals(failureCause, taskControl.getFailureCause());
    }

    @Test
    public void testCancelTask() throws Exception {

        final T taskExecutor = getAsynchronousSingleThreadTaskExecutor();
        final Context context = getContext();
        final Throwable failureCause = new Throwable();
        final long millisTimeout = 100;
        final long secTimeout = 1;

        final Task successfulTask = c -> {
            Phaser phaser = c.get(Phaser.class);
            phaser.arriveAndAwaitAdvance();
            phaser.arriveAndAwaitAdvance();
        };

        final Task failedTask = c -> {
            Phaser phaser = c.get(Phaser.class);
            phaser.arriveAndAwaitAdvance();
            phaser.arriveAndAwaitAdvance();
            throw failureCause;
        };

        final Supplier<InterruptableTask> successfulInterruptableTaskProvider =
                () -> new AbstractInterruptableTask() {
            @Override
            protected void doExecute(Context context) throws Throwable {
                Phaser phaser = context.get(Phaser.class);
                phaser.arriveAndAwaitAdvance();
                phaser.arriveAndAwaitAdvance();
                checkpoint();
            }
        };

        final Supplier<InterruptableTask> failedInterruptableTaskProvider =
                () -> new AbstractInterruptableTask() {
            @Override
            protected void doExecute(Context context) throws Throwable {
                Phaser phaser = context.get(Phaser.class);
                phaser.arriveAndAwaitAdvance();
                phaser.arriveAndAwaitAdvance();
                throw failureCause;
            }
        };

        final AtomicReference<TaskControl> taskControlRef = new AtomicReference<>();
        final AtomicReference<Phaser> phaserRef = new AtomicReference<>();

        final Consumer<Task> taskRunning = task -> {

            long start;
            TaskControl taskControl;
            Phaser phaser;

            context.clear();
            phaser = new Phaser(2);
            context.put(phaser);

            //for pending delay in single thread
            taskExecutor.execute(failedTask, context);
            assertEquals(1, phaser.arriveAndAwaitAdvance());

            taskControl = taskExecutor.execute(task, context);
            Assert.assertEquals(PENDING, taskControl.getTaskStage());
            assertNull(taskControl.getFailureCause());

            taskControl.cancelTask();

            try {
                start = System.currentTimeMillis();
                Assert.assertEquals(TaskControl.TaskStage.CANCELLED, taskControl.awaitNextStage(PENDING, millisTimeout));
                assertTrue(System.currentTimeMillis() - start < millisTimeout);
                Assert.assertEquals(TaskControl.TaskStage.CANCELLED, taskControl.getTaskStage());
                assertNull(taskControl.getFailureCause());
                assertEquals(2, phaser.arriveAndAwaitAdvance());
            } catch (UselessWaitingException | InterruptedException e) {
                throw new RuntimeException();
            }

            Assert.assertEquals(TaskControl.TaskStage.CANCELLED, taskControl.getTaskStage());

            context.clear();
            phaser = new Phaser(2);
            context.put(phaser);

            //for pending delay in single thread
            taskExecutor.execute(failedTask, context);
            assertEquals(1, phaser.arriveAndAwaitAdvance());

            taskControl = taskExecutor.execute(task, context);
            Assert.assertEquals(PENDING, taskControl.getTaskStage());
            assertNull(taskControl.getFailureCause());

            try {
                assertEquals(2, phaser.arriveAndAwaitAdvance());
                assertEquals(3, phaser.arriveAndAwaitAdvance());
                Assert.assertEquals(TaskControl.TaskStage.RUNNING, taskControl.awaitNextStage(PENDING));
                Assert.assertEquals(TaskControl.TaskStage.RUNNING, taskControl.getTaskStage());
                assertNull(taskControl.getFailureCause());
            } catch (UselessWaitingException | InterruptedException e) {
                throw new RuntimeException();
            }

            taskControl.cancelTask();

            taskControlRef.set(taskControl);
            phaserRef.set(phaser);
        };

        long start;
        TaskControl taskControl;
        Phaser phaser;

        //-------------------

        taskRunning.accept(successfulTask);

        taskControl = taskControlRef.getAndSet(null);
        phaser = phaserRef.getAndSet(null);

        assertEquals(4, phaser.arriveAndAwaitAdvance());
        Assert.assertEquals(TaskControl.TaskStage.COMPLETE, taskControl.awaitNextStage(TaskControl.TaskStage.RUNNING));
        Assert.assertEquals(TaskControl.TaskStage.COMPLETE, taskControl.awaitNextStage(PENDING));
        start = System.currentTimeMillis();
        Assert.assertEquals(TaskControl.TaskStage.COMPLETE, taskControl.awaitNextStage(TaskControl.TaskStage.RUNNING, secTimeout, TimeUnit.SECONDS));
        assertTrue(System.currentTimeMillis() - start < TimeUnit.SECONDS.toMillis(secTimeout));
        Assert.assertEquals(TaskControl.TaskStage.COMPLETE, taskControl.getTaskStage());
        assertNull(taskControl.getFailureCause());

        //-------------------

        taskRunning.accept(failedTask);

        taskControl = taskControlRef.getAndSet(null);
        phaser = phaserRef.getAndSet(null);

        assertEquals(4, phaser.arriveAndAwaitAdvance());
        Assert.assertEquals(FAILED, taskControl.awaitNextStage(TaskControl.TaskStage.RUNNING));
        Assert.assertEquals(FAILED, taskControl.awaitNextStage(PENDING));
        start = System.currentTimeMillis();
        Assert.assertEquals(FAILED, taskControl.awaitNextStage(TaskControl.TaskStage.RUNNING, secTimeout, TimeUnit.SECONDS));
        assertTrue(System.currentTimeMillis() - start < TimeUnit.SECONDS.toMillis(secTimeout));
        Assert.assertEquals(FAILED, taskControl.getTaskStage());
        assertEquals(failureCause, taskControl.getFailureCause());

        //-------------------

        taskRunning.accept(successfulInterruptableTaskProvider.get());

        taskControl = taskControlRef.getAndSet(null);
        phaser = phaserRef.getAndSet(null);

        assertEquals(4, phaser.arriveAndAwaitAdvance());
        Assert.assertEquals(TaskControl.TaskStage.CANCELLED, taskControl.awaitNextStage(TaskControl.TaskStage.RUNNING));
        Assert.assertEquals(TaskControl.TaskStage.CANCELLED, taskControl.awaitNextStage(PENDING));
        start = System.currentTimeMillis();
        Assert.assertEquals(TaskControl.TaskStage.CANCELLED, taskControl.awaitNextStage(TaskControl.TaskStage.RUNNING, secTimeout, TimeUnit.SECONDS));
        assertTrue(System.currentTimeMillis() - start < TimeUnit.SECONDS.toMillis(secTimeout));
        Assert.assertEquals(TaskControl.TaskStage.CANCELLED, taskControl.getTaskStage());
        assertNull(taskControl.getFailureCause());

        //-------------------

        taskRunning.accept(failedInterruptableTaskProvider.get());

        taskControl = taskControlRef.getAndSet(null);
        phaser = phaserRef.getAndSet(null);

        assertEquals(4, phaser.arriveAndAwaitAdvance());
        Assert.assertEquals(FAILED, taskControl.awaitNextStage(TaskControl.TaskStage.RUNNING));
        Assert.assertEquals(FAILED, taskControl.awaitNextStage(PENDING));
        start = System.currentTimeMillis();
        Assert.assertEquals(FAILED, taskControl.awaitNextStage(TaskControl.TaskStage.RUNNING, secTimeout, TimeUnit.SECONDS));
        assertTrue(System.currentTimeMillis() - start < TimeUnit.SECONDS.toMillis(secTimeout));
        Assert.assertEquals(FAILED, taskControl.getTaskStage());
        assertEquals(failureCause, taskControl.getFailureCause());
    }

    @Test
    public void testThreadDeath() throws Exception {

        BlockingQueue<Throwable> errorInterceptor = new SynchronousQueue<>();
        ThreadFactory threadFactory = new ThreadFactory() {
            private final ThreadFactory tf = Executors.defaultThreadFactory();

            @Override
            public Thread newThread(Runnable r) {
                Thread thr = tf.newThread(r);
                thr.setUncaughtExceptionHandler((t, e) -> {
                    try {
                        errorInterceptor.put(e);
                    } catch (InterruptedException interruptedException) {
                        //do nothing
                    }
                });
                return thr;
            }
        };
        T synchronousTaskExecutor = getSynchronousTaskExecutor();
        T asynchronousTaskExecutor = getAsynchronousSingleThreadTaskExecutor(threadFactory);
        ThreadDeath threadDeath = new ThreadDeath();
        Task successfulTask = context -> {};
        Task failedTask = context -> {throw threadDeath;};
        TaskControl taskControl;

        for (int i = 0; i < 3; i++) {
            taskControl = asynchronousTaskExecutor.execute(failedTask, null);
            assertEquals(FAILED, taskControl.awaitNextStage(RUNNING));
            assertEquals(threadDeath, taskControl.getFailureCause());
            assertEquals(threadDeath, errorInterceptor.take());
            taskControl = asynchronousTaskExecutor.execute(successfulTask, null);
            assertEquals(COMPLETE, taskControl.awaitNextStage(RUNNING));
            assertNull(taskControl.getFailureCause());
        }

        for (int i = 0; i < 3; i++) {
            try {
                synchronousTaskExecutor.execute(failedTask, null);
                fail();
            } catch (ThreadDeath e) {
                assertEquals(threadDeath, e);
            }
            taskControl = synchronousTaskExecutor.execute(successfulTask, null);
            assertEquals(COMPLETE, taskControl.awaitNextStage(RUNNING));
            assertNull(taskControl.getFailureCause());
        }
    }
}
