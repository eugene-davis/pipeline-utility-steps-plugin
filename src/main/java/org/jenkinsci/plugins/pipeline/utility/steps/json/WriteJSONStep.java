/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Nikolas Falco
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.jenkinsci.plugins.pipeline.utility.steps.json;

import com.google.common.collect.ImmutableSet;
import hudson.FilePath;
import hudson.model.TaskListener;
import org.jenkinsci.plugins.pipeline.utility.steps.AbstractWriteStep;
import org.jenkinsci.plugins.pipeline.utility.steps.AbstractWriteStepDescriptorImpl;

import org.jenkinsci.plugins.workflow.steps.Step;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepDescriptor;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import hudson.Extension;
import hudson.Util;
import net.sf.json.JSON;
import org.kohsuke.stapler.DataBoundSetter;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Set;

/**
 * Writes a {@link JSON} object to file in the current working directory.
 *
 * @author Nikolas Falco
 */
public class WriteJSONStep extends AbstractWriteStep {

    private final String file;
    private final JSON json;
    private int pretty = 0;

    @DataBoundConstructor
    public WriteJSONStep(String file, JSON json) {
        this.file = Util.fixNull(file);
        this.json = json;
    }

    /**
     * Return the JSON object to save.
     *
     * @return a {@link JSON} object
     */
    public JSON getJson() {
        return json;
    }

    /**
     * Return the number of spaces used to prettify the JSON dump.
     *
     * @return a int
     */
    public int getPretty() {
        return pretty;
    }

    /**
     * Indents to use if the JSON should be pretty printed.
     * A greater than zero integer will do so.
     *
     * @param pretty the indent size
     */
    @DataBoundSetter
    void setPretty(int pretty) {
        this.pretty = pretty;
    }

    @Override
    public StepExecution start(StepContext context) throws Exception {
        return new WriteJSONStepExecution(this, context);
    }

    @Extension
    public static class DescriptorImpl extends AbstractWriteStepDescriptorImpl {

        public DescriptorImpl() {

        }

        @Override
        public Set<? extends Class<?>> getRequiredContext() {
            return ImmutableSet.of(TaskListener.class, FilePath.class);
        }

        @Override
        public String getFunctionName() {
            return "writeJSON";
        }

        @Override
        @Nonnull
        public String getDisplayName() {
            return Messages.WriteJSONStep_DescriptorImpl_displayName();
        }

    }

}