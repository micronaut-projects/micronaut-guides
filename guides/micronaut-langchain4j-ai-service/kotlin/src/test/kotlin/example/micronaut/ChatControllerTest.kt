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

import dev.langchain4j.data.message.SystemMessage
import dev.langchain4j.data.message.UserMessage
import io.micronaut.context.annotation.Property
import io.micronaut.http.HttpRequest
import io.micronaut.http.MediaType
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@Property(name = "micronaut.http.client.read-timeout", value = "5m") // <1>
@MicronautTest // <2>
class ChatControllerTest {

    @Test
    fun conversationsHaveTheirOwnMemory(@Client("/") httpClient: HttpClient, // <3>
                                        recorder: ChatRequestRecorder) { // <4>
        val client = httpClient.toBlocking()

        val answer = chat(client, "sergio", "Hi, my name is Sergio. What is the Micronaut framework?")
        assertFalse(answer.isBlank()) // <5>

        chat(client, "sergio", "What is my name?")
        var messages = recorder.lastRequest()
        assertEquals(4, messages.size) // <6>
        assertInstanceOf(SystemMessage::class.java, messages.first())
        assertTrue((messages[1] as UserMessage).singleText().contains("Sergio"))

        chat(client, "tim", "What is my name?")
        messages = recorder.lastRequest()
        assertEquals(2, messages.size) // <7>
        assertTrue(messages.none { it.toString().contains("Sergio") })
    }

    private fun chat(client: BlockingHttpClient, conversationId: String, message: String): String {
        val request = HttpRequest.POST("/chat/$conversationId", message)
            .contentType(MediaType.TEXT_PLAIN_TYPE)
        return client.retrieve(request)
    }
}
