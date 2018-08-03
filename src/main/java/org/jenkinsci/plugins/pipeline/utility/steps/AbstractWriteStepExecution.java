package org.jenkinsci.plugins.pipeline.utility.steps;

import hudson.FilePath;
import hudson.model.TaskListener;
import org.jenkinsci.plugins.workflow.steps.MissingContextVariableException;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.SynchronousNonBlockingStepExecution;

import java.io.FileNotFoundException;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static org.apache.commons.lang.StringUtils.isBlank;

public abstract class AbstractWriteStepExecution<T> extends AbstractFileStepExecution<T> {

    private transient AbstractWriteStep fileStep;

    protected FilePath ws;
    protected FilePath path;

    protected AbstractWriteStepExecution(@Nonnull AbstractWriteStep step, @Nonnull StepContext context) {
        super(step, context);
        this.fileStep = step;
    }

    @Override
    protected final T run() throws Exception {
        FilePath ws = getContext().get(FilePath.class);
        assert ws != null;

        if (isBlank(fileStep.getFile())) {
            throw new IllegalArgumentException(Messages.AbstractFileStepDescriptorImp_missingFile(this.fileStep.getDescriptor().getFunctionName()));
        }

        this.path = ws.child(fileStep.getFile());
        if (path.isDirectory()) {
            throw new FileNotFoundException(Messages.AbstractFileStepExecution_fileIsDirectory(path.getRemote()));
        }

        return super.run();
    }

}
