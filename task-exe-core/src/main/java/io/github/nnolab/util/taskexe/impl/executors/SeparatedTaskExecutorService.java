package io.github.nnolab.util.taskexe.impl.executors;

import io.github.nnolab.util.context.Context;
import io.github.nnolab.util.taskexe.*;
import io.github.nnolab.util.taskexe.impl.executors.shells.TaskControlShell;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import static io.github.nnolab.util.taskexe.TaskControl.TaskStage.*;

/**
 * A {@link ExecutorProxyTaskExecutorService} with separated task controls
 * iterator returned by {@link #getTasks()}.
 *
 * @author nnolab
 */
public class SeparatedTaskExecutorService extends ExecutorProxyTaskExecutorService {

    private Set<InnerTaskControl> taskControls = new HashSet<>();

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
    public SeparatedTaskExecutorService(Executor executor, boolean tryShutdownExecutor) {
        super(executor, tryShutdownExecutor);
    }

    /**
     * Create task executor service with specified {@link Executor}
     * for execution delegation.
     *
     * @param executor executor to delegate tasks execution
     * @throws NullPointerException if specified executor is {@code null}
     */
    public SeparatedTaskExecutorService(Executor executor) {
        super(executor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected InnerTaskControl createTaskControl(Task task, Context context) {
        InnerTaskControl taskControl = new InnerTaskControl(task, context, this);
        taskControls.add(taskControl);
        return taskControl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void utilizeTaskControl(InnerTaskControl taskControl) {
        taskControls.remove(taskControl);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void cancelAllPendingTasks() {
        for (InnerTaskControl taskControl : taskControls) {
            taskControl.tryUpdateTaskStage(PENDING, CANCELLED);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean checkAllTasksFinished() {
        for (InnerTaskControl taskControl : taskControls) {
            TaskControl.TaskStage taskStage = taskControl.getTaskStage();
            if (taskStage != COMPLETE && taskStage != CANCELLED) {
                return false;
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTaskCount() {
        return taskControls.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tasks getTasks() {
        lock.lock();
        try {
            SeparatedTasks tasks = new SeparatedTasks(taskControls.size());
            for (InnerTaskControl taskControl : taskControls) {
                tasks.add(new TaskControlShell(taskControl));
            }
            return tasks;
        } finally {
            lock.unlock();
        }
    }
}
