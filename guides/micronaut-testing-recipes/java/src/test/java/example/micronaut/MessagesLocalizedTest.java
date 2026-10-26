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

import io.micronaut.core.io.ResourceLoader;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false) // <1>
class MessagesLocalizedTest {

    private static final List<String> SUFFIXES = List.of("es"); // <2>

    @Test
    void keysAreLocalizedInEveryLanguage(ResourceLoader resourceLoader) throws IOException { // <3>
        Set<String> keys = keys(resourceLoader, "classpath:i18n/messages_en.properties");
        for (String suffix : SUFFIXES) {
            Set<String> keysLocalized = keys(resourceLoader, "classpath:i18n/messages_" + suffix + ".properties");
            assertEquals(keys, keysLocalized); // <4>
        }
    }

    private static Set<String> keys(ResourceLoader resourceLoader, String path) throws IOException {
        Optional<InputStream> inputStreamOptional = resourceLoader.getResourceAsStream(path);
        assertTrue(inputStreamOptional.isPresent());
        try (InputStream inputStream = inputStreamOptional.get()) {
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties.keySet().stream()
                    .map(Object::toString)
                    .collect(Collectors.toSet());
        }
    }
}
