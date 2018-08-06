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

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;

public abstract class AbstractWriteStepExecution extends AbstractFileStepExecution<String> {

    private transient AbstractWriteStep fileStep;

    protected FilePath ws;
    protected FilePath path;

    protected AbstractWriteStepExecution(@Nonnull AbstractWriteStep step, @Nonnull StepContext context) {
        super(step, context);
        this.fileStep = step;
    }

    protected abstract String encode() throws Exception;

    @Override
    protected String doRun() throws Exception {
        this.ws = getContext().get(FilePath.class);
        String encodedData = this.encode();
        assert ws != null;

        if (isBlank(fileStep.getFile()) && !fileStep.getReturnString()) {
            throw new IllegalArgumentException(Messages.AbstractFileStepDescriptorImp_missingFile(this.fileStep.getDescriptor().getFunctionName()));
        }

        if (isNotBlank(fileStep.getFile())) {
            this.path = ws.child(fileStep.getFile());
            if (path.isDirectory()) {
                throw new FileNotFoundException(Messages.AbstractFileStepExecution_fileIsDirectory(path.getRemote()));
            }
    
            try (OutputStreamWriter writer = new OutputStreamWriter(this.path.write(), "UTF-8")) {
                writer.write(encodedData);
            }
        }

        if (fileStep.getReturnString()) {
            return encodedData;
        }
        
        return null;
    }

}
