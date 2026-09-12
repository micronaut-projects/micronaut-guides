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

import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@MicronautTest // <1>
class StaticResourceTest {

    @Inject
    @field:Client("/css") // <2>
    lateinit var cssClient: HttpClient

    @Inject
    @field:Client("/images") // <2>
    lateinit var imageClient: HttpClient

    @Test
    fun stylesheetExists() {
        val css = cssClient.toBlocking().retrieve("/style.css", String::class.java)
        assertTrue(css.contains("html, body {"))
    }

    @Test
    fun imageExists() {
        val image = imageClient.toBlocking().retrieve("/micronaut_stacked_black.png", ByteArray::class.java)
        val expectedLength = StaticResourceTest::class.java
            .getResourceAsStream("/static/images/micronaut_stacked_black.png")!!
            .use { it.readBytes().size }
        assertEquals(expectedLength, image.size)
    }
}
