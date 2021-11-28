package io.github.nnolab.util.taskexe.impl.executors;

import io.github.nnolab.util.context.Context;
import io.github.nnolab.util.taskexe.*;
import io.github.nnolab.util.taskexe.impl.executors.shells.TaskControlShell;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;

/**
 * An abstract {@link TaskExecutorService} with common functional.
 *
 * @author nnolab
 */
public abstract class AbstractTaskExecutorService implements TaskExecutorService {

    /**
     * A {@link TaskControlImpl}, bounded to task executor state.
     */
    protected class InnerTaskControl extends TaskControlImpl {

        /**
         * Create new control with given task, context and executor.
         *
         * @param task     task
         * @param context  context
         * @param executor executor
         */
        public InnerTaskControl(Task task, Context context, TaskExecutor executor) {
            super(task, context, executor);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void cancelTask() {
            super.cancelTask();
            AbstractTaskExecutorService.this.lock.lock();
            try {
                AbstractTaskExecutorService.this.statePoint = new Object();
                AbstractTaskExecutorService.this.condition.signalAll();
            } finally {
                AbstractTaskExecutorService.this.lock.unlock();
            }
        }
    }

    /**
     * A {@link Tasks} implementation, separated of executor state.
     */
    protected static class SeparatedTasks implements Iterator<TaskControl>, Tasks {

        private TaskControl[] taskControls;
        int cur;
        int end;

        /**
         * Create new iterator with given initial capacity.
         *
         * @param capacity initial capacity
         */
        public SeparatedTasks(int capacity) {
            taskControls = new TaskControl[capacity];
            cur = end = -1;
        }

        public void add(TaskControl taskControl) {
            int i = end + 1;
            if (i >= taskControls.length) {
                taskControls = new TaskControl[i + 1];
            }
            end = i;
            taskControls[i] = taskControl;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean hasNext() {
            return cur < end;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public TaskControl next() {
            if (cur >= end) {
                throw new NoSuchElementException();
            }
            return taskControls[++cur];
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Iterator<TaskControl> iterator() {
            return this;
        }
    }

    /**
     * A {@link InnerTaskControl}, able to be chained with others.
     */
    protected class TaskControlNode extends InnerTaskControl {

        /**
         * Previous node.
         */
        public volatile TaskControlNode prev;

        /**
         * Next node.
         */
        public volatile TaskControlNode next;

        /**
         * {@code true} if node already removed or {@code false} otherwise
         */
        public volatile boolean removed = false;

        /**
         * Create new control node with given task, context and executor.
         *
         * @param task     task
         * @param context  context
         * @param executor executor
         */
        public TaskControlNode(Task task, Context context, TaskExecutor executor) {
            super(task, context, executor);
        }

        /**
         * Remove node from chain.
         * Next and previous nodes, if exists
         * will lose reference to it.
         */
        public void remove() {
            if (prev != null) {
                prev.next = next;
            }
            if (next != null) {
                next.prev = prev;
            }
            removed = true;
        }
    }

    /**
     * A {@link Tasks} implementation that uses chained task controls
     * to iterate.
     */
    protected static class LinkedTasks implements Iterator<TaskControl>, Tasks {

        private volatile TaskControlNode cur;
        private volatile TaskControlNode next;

        /**
         * Construct iterator with zero node.
         * Pointer is set on zero node.
         * Zero node never returned by {@link #next()}
         *
         * @param zero zero node
         */
        public LinkedTasks(TaskControlNode zero) {
            cur = zero;
            next = cur.next;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean hasNext() {
            next = cur.next;
            return next != null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public TaskControl next() {
            if (next == null) {
                throw new NoSuchElementException();
            }
            TaskControlNode node = next;
            cur = next;
            next = cur.next;
            return new TaskControlShell(node);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Iterator<TaskControl> iterator() {
            return this;
        }
    }

    protected final Lock lock = new ReentrantLock(true);
    protected final Condition condition = lock.newCondition();

    protected volatile boolean terminating = false;
    protected volatile boolean terminated = false;

    protected volatile Object statePoint = new Object();

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTerminating() {
        return terminating;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTerminated() {
        return terminated;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void awaitTermination() throws InterruptedException {
        lock.lock();
        try {
            while (!terminated) {
                condition.await();
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean awaitTermination(long timeout) throws InterruptedException {
        return awaitTermination(timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        if (timeout < 0) {
            throw new IllegalArgumentException("Invalid timeout: " + timeout);
        }
        lock.lock();
        try {
            long nanosTimeout = unit.toNanos(timeout);
            if (nanosTimeout < 0) {
                nanosTimeout = Long.MAX_VALUE;
            }
            while (!terminated) {
                if (nanosTimeout <= 0) {
                    return false;
                }
                nanosTimeout = condition.awaitNanos(nanosTimeout);
            }
            return true;
        } finally {
            lock.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object awaitAction(Object statePoint) throws InterruptedException {
        lock.lock();
        try {
            while (statePoint == this.statePoint) {
                condition.await();
            }
            return this.statePoint;
        } finally {
            lock.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object awaitAction(Object statePoint, long timeout) throws InterruptedException {
        return awaitAction(statePoint, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object awaitAction(Object statePoint, long timeout, TimeUnit unit) throws InterruptedException {
        if (timeout < 0) {
            throw new IllegalArgumentException("Invalid timeout: " + timeout);
        }
        lock.lock();
        try {
            long nanosTimeout = unit.toNanos(timeout);
            if (nanosTimeout < 0) {
                nanosTimeout = Long.MAX_VALUE;
            }
            while (statePoint == this.statePoint && nanosTimeout > 0) {
                nanosTimeout = condition.awaitNanos(nanosTimeout);
            }
            return this.statePoint;
        } finally {
            lock.unlock();
        }
    }
}
