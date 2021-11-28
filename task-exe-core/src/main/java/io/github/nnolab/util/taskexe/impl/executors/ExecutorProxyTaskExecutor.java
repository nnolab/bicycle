package io.github.nnolab.util.taskexe.impl.executors;

import io.github.nnolab.util.context.Context;
import io.github.nnolab.util.taskexe.*;
import io.github.nnolab.util.taskexe.impl.executors.shells.TaskControlShell;

import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * A {@link TaskExecutor} that wraps submitted task in {@link Runnable}
 * and delegates execution to inner {@link Executor}.
 *
 * @author nnolab
 */
public class ExecutorProxyTaskExecutor implements TaskExecutor {

    private final Executor executor;

    /**
     * Create task executor with given proxy executor.
     *
     * @param executor proxy executor
     * @throws NullPointerException if specified executor is {@code null}
     */
    public ExecutorProxyTaskExecutor(Executor executor) {
        this.executor = Objects.requireNonNull(executor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskControl execute(Task task, Context context) {
        TaskControlImpl taskControl = new TaskControlImpl(task, context, this);
        executor.execute(() -> {
            if (taskControl.tryUpdateTaskStage(TaskControl.TaskStage.PENDING, TaskControl.TaskStage.RUNNING)) {
                try {
                    //---------------------
                    task.execute(context);
                    //---------------------
                    if ((task instanceof InterruptableTask)
                            && ((InterruptableTask) task).isInterrupted()) {
                        taskControl.updateTaskStage(TaskControl.TaskStage.CANCELLED);
                    } else {
                        taskControl.updateTaskStage(TaskControl.TaskStage.COMPLETE);
                    }
                } catch (Throwable e) {
                    taskControl.setFailureCause(e);
                    taskControl.updateTaskStage(TaskControl.TaskStage.FAILED);
                    if (e instanceof ThreadDeath) {
                        throw (ThreadDeath) e;
                    }
                }
            }
        });
        return new TaskControlShell(taskControl);
    }
}
