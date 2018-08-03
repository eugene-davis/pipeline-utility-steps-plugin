package org.jenkinsci.plugins.pipeline.utility.steps;

import hudson.model.TaskListener;
import org.jenkinsci.plugins.workflow.steps.StepDescriptor;
import org.jenkinsci.plugins.workflow.steps.StepExecution;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang.StringUtils.isBlank;

public abstract class AbstractWriteStepDescriptorImpl extends StepDescriptor {

    protected AbstractWriteStepDescriptorImpl() {
    }

    @Override
    public Set<? extends Class<?>> getRequiredContext() {
        return Collections.singleton(TaskListener.class);
    }

    @Override
    public AbstractWriteStep newInstance(Map<String, Object> arguments) throws Exception {
        AbstractWriteStep step = (AbstractWriteStep) super.newInstance(arguments);
        if (isBlank(step.getFile()) && !step.returnString) {
            throw new IllegalArgumentException(Messages.AbstractFileStepDescriptorImp_missingFile(getFunctionName()));
        }
        return step;
    }
}