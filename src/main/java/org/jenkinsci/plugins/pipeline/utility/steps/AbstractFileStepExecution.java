package org.jenkinsci.plugins.pipeline.utility.steps;

import hudson.FilePath;
import hudson.model.TaskListener;
import org.jenkinsci.plugins.workflow.steps.AbstractSynchronousNonBlockingStepExecution;
import org.jenkinsci.plugins.workflow.steps.MissingContextVariableException;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.SynchronousNonBlockingStepExecution;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static org.apache.commons.lang.StringUtils.isNotBlank;

public abstract class AbstractFileStepExecution<T> extends SynchronousNonBlockingStepExecution<T> {

    private transient AbstractFileStep fileStep;

    protected FilePath ws;

    protected AbstractFileStepExecution(@Nonnull AbstractFileStep step, @Nonnull StepContext context) {
        super(context);
        this.fileStep = step;
    }

    @Override
    protected T run() throws Exception {
        ws = getContext().get(FilePath.class);
        if (ws == null && isNotBlank(fileStep.getFile())) {
            throw new MissingContextVariableException(FilePath.class);
        }
        return doRun();
    }

    protected abstract T doRun() throws Exception;
}
