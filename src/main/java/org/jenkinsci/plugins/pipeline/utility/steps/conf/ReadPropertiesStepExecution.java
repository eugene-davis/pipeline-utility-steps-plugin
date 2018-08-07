/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 CloudBees Inc.
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

package org.jenkinsci.plugins.pipeline.utility.steps.conf;

import org.apache.commons.configuration2.AbstractConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.ConfigurationConverter;
import org.apache.commons.lang.StringUtils;
import org.jenkinsci.plugins.pipeline.utility.steps.AbstractReadStepExecution;
import org.jenkinsci.plugins.workflow.steps.StepContext;

import javax.annotation.Nonnull;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Execution of {@link ReadPropertiesStep}.
 *
 * @author Robert Sandell &lt;rsandell@cloudbees.com&gt;.
 */
public class ReadPropertiesStepExecution extends AbstractReadStepExecution<Map<String, Object>> {
    private static final long serialVersionUID = 1L;

    private transient ReadPropertiesStep step;

    protected ReadPropertiesStepExecution(@Nonnull ReadPropertiesStep step, @Nonnull StepContext context) {
        super(step, context);
        this.step = step;
    }

    protected Map<String, Object> decode(String text) throws Exception {
        Properties properties = new Properties();
        StringReader sr = new StringReader(text);
        properties.load(sr);

        if (StringUtils.isNotBlank(step.getText())) {
            sr = new StringReader(step.getText());
            properties.load(sr);
            // In the case that we did this, set text blank to avoid re-processing by parent
            step.setText("");
        }

        // Check if we should interpolated values in the properties
        if ( step.isInterpolate() ) {
            properties = interpolateProperties(properties);
        }

        Map<String, Object> result = new HashMap<>();
        addAll(step.getDefaults(), result);
        addAll(properties, result);
        return result;
    }

    /**
     * addAll implementation that will coerce keys into Strings.
     *
     * @param src the source
     * @param dst the destination
     */
    private void addAll(Map src, Map<String, Object> dst) {
        if (src == null) {
            return;
        }

        for (Map.Entry e : (Set<Map.Entry>) src.entrySet()) {
            dst.put(e.getKey() != null ? e.getKey().toString(): null, e.getValue());
        }
    }

    /**
     * Using commons collection to interpolated the values inside the properties
     * @param properties the list of properties to be interpolated
     * @return a new Properties object with the interpolated values
     */
    private Properties interpolateProperties(Properties properties) throws Exception {
        if ( properties == null)
            return null;
        Configuration interpolatedProp;
        // Convert the Properties to a Configuration object in order to apply the interpolation
        Configuration conf = ConfigurationConverter.getConfiguration(properties);

        // Apply interpolation
        interpolatedProp = ((AbstractConfiguration)conf).interpolatedConfiguration();

        // Convert back to properties
        return ConfigurationConverter.getProperties(interpolatedProp);
    }
}
