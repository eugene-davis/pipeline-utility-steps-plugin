package org.jenkinsci.plugins.pipeline.utility.steps;

import hudson.model.TaskListener;
import org.jenkinsci.plugins.workflow.steps.StepDescriptor;
import org.jenkinsci.plugins.workflow.steps.StepExecution;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang.StringUtils.isBlank;

public abstract class AbstractReadStepDescriptorImpl extends StepDescriptor {

    protected AbstractReadStepDescriptorImpl() {
    }

    @Override
    public Set<? extends Class<?>> getRequiredContext() {
        return Collections.singleton(TaskListener.class);
    }

    @Override
    public AbstractReadStep newInstance(Map<String, Object> arguments) throws Exception {
        AbstractReadStep step = (AbstractReadStep) super.newInstance(arguments);
        if (isBlank(step.getFile()) && isBlank(step.getText())) {
            throw new IllegalArgumentException(Messages.AbstractFileStepDescriptorImpl_missingRequiredArgument(getFunctionName()));
        }
        return step;
    }
}