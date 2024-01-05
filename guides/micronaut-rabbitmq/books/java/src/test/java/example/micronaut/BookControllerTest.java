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

import io.micronaut.context.annotation.Primary;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.inject.Inject;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@MicronautTest
public class BookControllerTest {

    @Inject
    AnalyticsClient analyticsClient;

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void testMessageIsPublishedToRabbitMQWhenBookFound() {
        Optional<Book> result = retrieveGet("/books/1491950358");

        assertNotNull(result);
        assertTrue(result.isPresent());

        verify(analyticsClient, times(1)).updateAnalytics(result.get());
    }

    @Test
    void testMessageIsNotPublishedToRabbitMQWhenBookNotFound() {
        assertThrows(HttpClientResponseException.class, () -> {
            Optional<Book> result = retrieveGet("/books/INVALID");

            assertNotNull(result);
            assertFalse(result.isPresent());
        });

        verifyNoInteractions(analyticsClient);
    }

    @Primary
    @MockBean
    AnalyticsClient analyticsClient() {
        return Mockito.mock(AnalyticsClient.class);
    }

    private Optional<Book> retrieveGet(String url) {
        return client.toBlocking().retrieve(HttpRequest.GET(url), Argument.of(Optional.class, Book.class));
    }

}
