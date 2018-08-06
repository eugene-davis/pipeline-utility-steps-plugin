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

import java.io.FileNotFoundException;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.jenkinsci.plugins.pipeline.utility.steps.AbstractWriteStepExecution;
import org.jenkinsci.plugins.workflow.steps.MissingContextVariableException;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepContextParameter;

import hudson.FilePath;
import org.jenkinsci.plugins.workflow.steps.SynchronousNonBlockingStepExecution;

import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * Execution of {@link WriteJSONStep}.
 *
 * @author Nikolas Falco
 */
public class WriteJSONStepExecution extends AbstractWriteStepExecution {
    private static final long serialVersionUID = 1L;

    private transient WriteJSONStep step;

    protected WriteJSONStepExecution(@Nonnull WriteJSONStep step, @Nonnull StepContext context) {
        super(step, context);
        this.step = step;
    }

    protected String encode() throws Exception {
        String json;
        if (step.getJson() == null) {
            throw new IllegalArgumentException(Messages.WriteJSONStepExecution_missingJSON(step.getDescriptor().getFunctionName()));
        }
        if (step.getPretty() > 0) {
            json = step.getJson().toString(step.getPretty());
        } else {
            json = step.getJson().toString();
        }
        return json;
    }
}