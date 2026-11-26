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

import io.micronaut.context.annotation.Property;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Property(name = "micronaut.http.client.read-timeout", value = "5m") // <1>
@MicronautTest // <2>
class SupportControllerTest {

    @Test
    void answersWithTheRetrievedDocuments(@Client("/") HttpClient httpClient) { // <3>
        HttpRequest<String> request = HttpRequest.POST("/support", "Can I take my cat to the Moon?")
                .contentType(MediaType.TEXT_PLAIN_TYPE);

        Answer answer = httpClient.toBlocking().retrieve(request, Answer.class);

        assertFalse(answer.answer().isBlank()); // <4>
        assertTrue(answer.sources().contains("pets.txt")); // <5>
    }
}
