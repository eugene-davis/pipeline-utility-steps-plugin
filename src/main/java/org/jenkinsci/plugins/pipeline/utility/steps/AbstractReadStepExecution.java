package org.jenkinsci.plugins.pipeline.utility.steps;

import hudson.FilePath;
import java.io.OutputStreamWriter;
import hudson.model.TaskListener;
import org.jenkinsci.plugins.workflow.steps.MissingContextVariableException;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.SynchronousNonBlockingStepExecution;

import java.io.FileNotFoundException;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;

public abstract class AbstractReadStepExecution<T> extends AbstractFileStepExecution<T> {

    private transient AbstractReadStep fileStep;

    protected FilePath path;

    protected AbstractReadStepExecution(@Nonnull AbstractReadStep step, @Nonnull StepContext context) {
        super(step, context);
        this.fileStep = step;
    }

    protected abstract T decode(String text) throws Exception;

    @Override
    protected T doRun() throws Exception {
        String fName = fileStep.getDescriptor().getFunctionName();
        if (isNotBlank(fileStep.getFile()) && isNotBlank(fileStep.getText())) {
            throw new IllegalArgumentException(Messages.AbstractReadStepExecution_tooManyArguments(fName));
        }

        T decodedObject = null;
        if (!isBlank(fileStep.getFile())) {
            FilePath f = this.ws.child(fileStep.getFile());
            if (f.exists() && !f.isDirectory()) {
                try (InputStream is = f.read()) {
                    decodedObject = this.decode(IOUtils.toString(is, "UTF-8"));
                }
            } else if (f.isDirectory()) {
                throw new IllegalArgumentException(org.jenkinsci.plugins.pipeline.utility.steps.Messages.AbstractFileStepExecution_fileIsDirectory(f.getRemote()));
            } else if (!f.exists()) {
                throw new FileNotFoundException(Messages.AbstractReadStepExecution_fileNotFound(f.getRemote()));
            }
        }
        if (!isBlank(fileStep.getText())) {
            decodedObject = this.decode(fileStep.getText().trim());
        }

        return decodedObject;
    }

}
