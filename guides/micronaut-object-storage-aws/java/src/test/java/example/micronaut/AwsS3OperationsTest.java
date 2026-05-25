/*
 * Copyright 2017-2026 original authors
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

import io.micronaut.context.BeanContext;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.objectstorage.aws.AwsS3Operations;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AwsS3OperationsTest implements TestPropertyProvider {

    @Inject
    BeanContext beanContext;

    @Test
    void onlyABeanOfTypeAwsS3OperationsExists() {
        assertDoesNotThrow(() -> beanContext.getBean(AwsS3Operations.class));
        assertEquals(1, beanContext.getBeansOfType(AwsS3Operations.class).size());
    }

    @Override
    @NonNull
    public Map<String, String> getProperties() {
        return CollectionUtils.mapOf(
                "aws.accessKeyId", "test",
                "aws.secretKey", "test"
        );
    }
}
