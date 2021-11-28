package io.github.nnolab.util.taskexe.impl.executors;

import io.github.nnolab.util.context.Context;
import io.github.nnolab.util.taskexe.*;
import io.github.nnolab.util.taskexe.exceptions.UselessWaitingException;

import java.util.Objects;

import static io.github.nnolab.util.taskexe.TaskControl.TaskStage.*;

/**
 * A {@link TaskExecutor}, that executes task synchronously
 * and return {@link TaskControl} with finished task.
 *
 * @author nnolab
 */
public class SynchronousTaskExecutor implements TaskExecutor {

    /**
     * Simple control for already finished task.
     */
    private static class PojoTaskControl implements TaskControl {

        private final Task task;
        private final Context context;
        private final TaskExecutor executor;

        private volatile TaskStage finalStage;
        private volatile Throwable failureCause;

        PojoTaskControl(Task task, Context context, TaskExecutor executor) {
            this.task = task;
            this.context = context;
            this.executor = executor;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Task getTask() {
            return task;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Context getContext() {
            return context;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public TaskExecutor getExecutor() {
            return executor;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public TaskStage getTaskStage() {
            return finalStage;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Throwable getFailureCause() {
            return failureCause;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public TaskStage awaitNextStage(TaskStage startStage)
                throws UselessWaitingException {
            if (!startStage.isBefore(finalStage)) {
                throw new UselessWaitingException(startStage);
            }
            return finalStage;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public TaskStage awaitNextStage(TaskStage startStage, long timeout)
                throws UselessWaitingException {
            if (!startStage.isBefore(finalStage)) {
                throw new UselessWaitingException(startStage);
            }
            return finalStage;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void cancelTask() {
            //do nothing
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskControl execute(Task task, Context context) {
        Objects.requireNonNull(task);
        PojoTaskControl control = new PojoTaskControl(task, context, this);
        try {
            task.execute(context);
            control.finalStage = COMPLETE;
        } catch (Throwable e) {
            control.finalStage = FAILED;
            control.failureCause = e;
            if (e instanceof ThreadDeath) {
                throw (ThreadDeath) e;
            }
        }
        return control;
    }
}
