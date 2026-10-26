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
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Property(name = "spec.name", value = "ContactControllerTest") // <1>
@MicronautTest // <2>
class ContactControllerTest {

    @Test
    void contactsAreRenderedAsHtml(@Client("/") HttpClient httpClient) { // <3>
        BlockingHttpClient client = httpClient.toBlocking();
        HttpRequest<?> request = HttpRequest.GET("/contacts").accept(MediaType.TEXT_HTML); // <4>
        String html = assertDoesNotThrow(() -> client.retrieve(request, String.class)); // <5>
        assertTrue(html.contains("<!DOCTYPE html>"));
        assertTrue(html.contains("Katherine"));
        assertTrue(html.contains("Adams"));
        assertTrue(html.contains("Tim"));
        assertTrue(html.contains("Cook"));
    }

    @Requires(property = "spec.name", value = "ContactControllerTest") // <1>
    @Replaces(ContactRepository.class) // <6>
    @Singleton
    static class ContactRepositoryReplacement implements ContactRepository {
        private final List<Contact> contacts = List.of(
                new Contact("Katherine", "Adams", "Apple Inc."),
                new Contact("Tim", "Cook", "Apple Inc."));

        @Override
        @NonNull
        public List<Contact> findAll() {
            return contacts;
        }

        @Override
        @NonNull
        public Optional<Contact> findByFirstName(@NonNull String firstName) {
            return contacts.stream()
                    .filter(contact -> contact.firstName().equals(firstName))
                    .findFirst();
        }
    }
}
