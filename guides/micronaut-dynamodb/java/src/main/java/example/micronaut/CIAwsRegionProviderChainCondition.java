/*
 * Copyright 2017-2023 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.micronaut;

import io.micronaut.context.condition.Condition;
import io.micronaut.context.condition.ConditionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;

/**
 * @see <a href="https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/region-selection.html">Default region provider chain</a>
 */
public class CIAwsRegionProviderChainCondition implements Condition {
    private static final Logger LOG = LoggerFactory.getLogger(CIAwsRegionProviderChainCondition.class);

    @Override
    public boolean matches(ConditionContext context) {
        if (System.getenv("CI") == null) {
            LOG.info("CI environment variable not present - Condition fulfilled");
            return true;
        }
        if (System.getProperty("aws.region") != null) {
            LOG.info("aws.region system property present - Condition fulfilled");
            return true;
        }
        if (System.getenv("AWS_REGION") != null) {
            LOG.info("AWS_REGION environment variable present - Condition fulfilled");
            return true;
        }
        boolean result = System.getenv("HOME") != null && new File(System.getenv("HOME") + "/.aws/config").exists();
        if (result) {
            LOG.info("~/.aws/config file exists - Condition fulfilled");
        }
        return result;
    }
}
