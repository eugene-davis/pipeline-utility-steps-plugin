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

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import org.jenkinsci.plugins.pipeline.utility.steps.AbstractReadStepExecution;
import org.jenkinsci.plugins.workflow.steps.StepContext;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import javax.annotation.Nonnull;


/**
 * Execution of {@link ReadJSONStep}.
 *
 * @author Nikolas Falco
 */
public class ReadJSONStepExecution extends AbstractReadStepExecution<JSON> {
    private static final long serialVersionUID = 1L;

    private transient ReadJSONStep fileStep;

    protected ReadJSONStepExecution(@Nonnull ReadJSONStep step, @Nonnull StepContext context) {
        super(step, context);
        this.fileStep = step;
    }

    protected JSON decode(String text) throws Exception {
        return JSONSerializer.toJSON(text);
    }

    @Override
    protected JSON doRun() throws Exception {
        String fName = fileStep.getDescriptor().getFunctionName();
        if (isNotBlank(fileStep.getFile()) && isNotBlank(fileStep.getText())) {
            throw new IllegalArgumentException(Messages.ReadJSONStepExecution_tooManyArguments(fName));
        }
        return super.doRun();
    }
}