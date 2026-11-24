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
package example.micronaut

import io.micronaut.core.io.ResourceLoader
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.Properties

@MicronautTest(startApplication = false) // <1>
class MessagesLocalizedTest {

    private val suffixes = listOf("es") // <2>

    @Test
    fun keysAreLocalizedInEveryLanguage(resourceLoader: ResourceLoader) { // <3>
        val keys = keys(resourceLoader, "classpath:i18n/messages_en.properties")
        for (suffix in suffixes) {
            val keysLocalized = keys(resourceLoader, "classpath:i18n/messages_$suffix.properties")
            assertEquals(keys, keysLocalized) // <4>
        }
    }

    private fun keys(resourceLoader: ResourceLoader, path: String): Set<String> {
        val inputStreamOptional = resourceLoader.getResourceAsStream(path)
        assertTrue(inputStreamOptional.isPresent)
        return inputStreamOptional.get().use { inputStream ->
            val properties = Properties()
            properties.load(inputStream)
            properties.keys.map { it.toString() }.toSet()
        }
    }
}
