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

import dev.langchain4j.data.message.ChatMessage
import dev.langchain4j.data.message.SystemMessage
import dev.langchain4j.data.message.UserMessage
import io.micronaut.context.annotation.Property
import io.micronaut.http.HttpRequest
import io.micronaut.http.MediaType
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@Property(name = "micronaut.http.client.read-timeout", value = "5m") // <1>
@MicronautTest // <2>
class ChatControllerSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient httpClient // <3>

    @Inject
    ChatRequestRecorder recorder // <4>

    void "conversations have their own memory"() {
        given:
        BlockingHttpClient client = httpClient.toBlocking()

        when:
        String answer = chat(client, "sergio", "Hi, my name is Sergio. What is the Micronaut framework?")

        then:
        !answer.isBlank() // <5>

        when:
        chat(client, "sergio", "What is my name?")
        List<ChatMessage> messages = recorder.lastRequest()

        then:
        messages.size() == 4 // <6>
        messages.first() instanceof SystemMessage
        ((UserMessage) messages[1]).singleText().contains("Sergio")

        when:
        chat(client, "tim", "What is my name?")
        messages = recorder.lastRequest()

        then:
        messages.size() == 2 // <7>
        messages.every { !it.toString().contains("Sergio") }
    }

    private static String chat(BlockingHttpClient client, String conversationId, String message) {
        HttpRequest<String> request = HttpRequest.POST("/chat/" + conversationId, message)
                .contentType(MediaType.TEXT_PLAIN_TYPE)
        client.retrieve(request)
    }
}
