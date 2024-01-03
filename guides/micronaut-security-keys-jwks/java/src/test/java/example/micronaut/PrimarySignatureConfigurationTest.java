/*
 * Copyright 2017-2024 original authors
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

import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.context.scope.Refreshable;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static io.micronaut.core.annotation.AnnotationUtil.NAMED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
public class PrimarySignatureConfigurationTest {

    @Inject
    ApplicationContext applicationContext;

    @Test
    void primarySignatureConfigurationIsAnnotatedWithRefreshable() {
        assertTrue(applicationContext.getBeanDefinition(PrimarySignatureConfiguration.class)
                .getAnnotationNameByStereotype(Refreshable.class)
                .isPresent());
    }

    @Test
    void primarySignatureConfigurationIsAnnotatedNamedGenerator() {
        assertNotNull(applicationContext.getBeanDefinition(PrimarySignatureConfiguration.class).getAnnotationMetadata()
                .getAnnotation(NAMED));

        assertTrue(applicationContext.getBeanDefinition(PrimarySignatureConfiguration.class).getAnnotationMetadata()
                .getAnnotation(NAMED)
                .getValue(String.class).isPresent());

        assertEquals("generator", applicationContext.getBeanDefinition(PrimarySignatureConfiguration.class).getAnnotationMetadata()
                .getAnnotation(NAMED)
                .getValue(String.class).get());
    }
}
