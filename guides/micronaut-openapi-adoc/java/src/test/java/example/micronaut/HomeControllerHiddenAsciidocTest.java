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

import io.micronaut.core.io.ResourceLoader;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false) // <1>
class HomeControllerHiddenAsciidocTest {

    @Test
    void homeControllerIsHidden(ResourceLoader resourceLoader) throws IOException {
        Optional<InputStream> adocInputStream = resourceLoader.getResourceAsStream("META-INF/swagger/micronaut-guides-1.0.adoc");
        assertTrue(adocInputStream.isPresent());
        String adoc = new String(adocInputStream.get().readAllBytes(), StandardCharsets.UTF_8);
        assertFalse(adoc.contains("=== __GET__ `/`"));
    }
}
