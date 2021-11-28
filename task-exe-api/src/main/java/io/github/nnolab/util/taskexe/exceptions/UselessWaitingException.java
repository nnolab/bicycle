package io.github.nnolab.util.taskexe.exceptions;

import io.github.nnolab.util.taskexe.TaskControl;

/**
 * Thrown if trying to wait for next task stage with stage, that has no next stages.
 *
 * @author nnolab
 */
public class UselessWaitingException extends Exception {

    private static final long serialVersionUID = 2882527311378349947L;

    /**
     * Create exception with current stage,
     * that has no next stages.
     *
     * @param stage current stage
     */
    public UselessWaitingException(TaskControl.TaskStage stage) {
        super(stage.toString());
    }
}
