package org.jenkinsci.plugins.pipeline.utility.steps;

import org.kohsuke.stapler.DataBoundSetter;

public abstract class AbstractWriteStep extends AbstractFileStep {
    protected Boolean returnString = false;

    /**
     * A Boolean indicating if the step should return a string or write a file
     *
     * @return return value flag
     */
    public Boolean getReturnString() {
        return returnString;
    }

    /**
     * A Boolean indicating if the step should return a string or write a file
     *
     * @return return value flag
     */
    @DataBoundSetter
    public void setReturnString(Boolean returnString) {
        this.returnString = returnString;
    }
}