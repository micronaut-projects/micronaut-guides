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

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.BlockingHttpClient
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class HelloWorldControllerTest {

    @Test
    fun emulateCheckpoint() {
        CheckpointTestUtils.test(::testHelloWorld)
    }

    private fun testHelloWorld(client: BlockingHttpClient) {
        val response = client.exchange(
            HttpRequest.GET<Any>("/"),
            Argument.mapOf(String::class.java, String::class.java)
        )
        assertEquals(HttpStatus.OK, response.status)
        val bodyOptional = response.body
        assertTrue(bodyOptional.isPresent)
        assertEquals(mapOf("message" to "Hello World"), bodyOptional.get())
    }
}
