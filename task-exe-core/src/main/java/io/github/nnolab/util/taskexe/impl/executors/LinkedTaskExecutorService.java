package io.github.nnolab.util.taskexe.impl.executors;

import io.github.nnolab.util.context.Context;
import io.github.nnolab.util.taskexe.*;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import static io.github.nnolab.util.taskexe.TaskControl.TaskStage.*;

/**
 * A {@link ExecutorProxyTaskExecutorService} with linked task controls
 * iterator returned by {@link #getTasks()}.
 * Task controls are iterated in order, they were submitted.
 *
 * @author nnolab
 */
public class LinkedTaskExecutorService extends ExecutorProxyTaskExecutorService {

    private volatile int taskCount = 0;
    private final TaskControlNode head = new TaskControlNode(null, null, null);
    private volatile TaskControlNode tail = head;

    /**
     * Create task executor service with specified {@link Executor}
     * for execution delegation.
     * If {@code tryShutdownExecutor} flag is {@code true} and specified
     * executor is instance of {@link ExecutorService},
     * than {@link ExecutorService#shutdown()} will be invoked
     * at invocation of {@link #shutdown()} and
     * {@link ExecutorService#shutdownNow()} will be invoked
     * at invocation of {@link #shutdownNow()}
     *
     * @param executor            executor to delegate tasks execution
     * @param tryShutdownExecutor shutdown executor flag
     * @throws NullPointerException if specified executor is {@code null}
     */
    public LinkedTaskExecutorService(Executor executor, boolean tryShutdownExecutor) {
        super(executor, tryShutdownExecutor);
    }

    /**
     * Create task executor service with specified {@link Executor}
     * for execution delegation.
     *
     * @param executor executor to delegate tasks execution
     * @throws NullPointerException if specified executor is {@code null}
     */
    public LinkedTaskExecutorService(Executor executor) {
        super(executor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected InnerTaskControl createTaskControl(Task task, Context context) {
        TaskControlNode node = new TaskControlNode(task, context, this);
        if (++taskCount < 0) {
            taskCount--;
            throw new Error("Task count overflow");
        }
        tail.next = node;
        node.prev = tail;
        tail = node;
        return node;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void utilizeTaskControl(InnerTaskControl taskControl) {
        TaskControlNode node = (TaskControlNode) taskControl;
        if (node == tail) {
            tail = node.prev;
        }
        if (!node.removed) {
            node.remove();
            taskCount--;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void cancelAllPendingTasks() {
        TaskControlNode node = head.next;
        while (node != null) {
            node.tryUpdateTaskStage(PENDING, CANCELLED);
            node = node.next;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean checkAllTasksFinished() {
        TaskControlNode node = head.next;
        while (node != null) {
            TaskControl.TaskStage taskStage = node.getTaskStage();
            if (taskStage != COMPLETE && taskStage != CANCELLED) {
                return false;
            }
            node = node.next;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTaskCount() {
        return taskCount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tasks getTasks() {
        return new LinkedTasks(head);
    }
}
