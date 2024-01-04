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

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest // <1>
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // <2>
class FruitDuplicationExceptionHandlerTest extends BaseTest {
    @Inject
    @Client("/")
    HttpClient httpClient; // <3>

    @Test
    void duplicatedFruitsReturns400() {
        FruitCommand banana = new FruitCommand("Banana");
        HttpRequest<?> request = HttpRequest.POST("/fruits", banana);
        HttpResponse<?> response = httpClient.toBlocking().exchange(request);
        assertEquals(HttpStatus.CREATED, response.status());
        HttpClientResponseException exception = assertThrows(
                HttpClientResponseException.class,
                () -> httpClient.toBlocking().exchange(request)
        );
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getStatus());
        HttpRequest<?> deleteRequest = HttpRequest.DELETE("/fruits", banana);
        HttpResponse<?> deleteResponse = httpClient.toBlocking().exchange(deleteRequest);
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.status());
    }
}
