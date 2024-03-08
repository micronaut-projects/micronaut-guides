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
package example.micronaut.controller;

import example.micronaut.model.BookAvailability;
import example.micronaut.model.BookInfo;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest // <1>
public class BooksControllerTest {

    @Inject
    @Client("${context-path}")
    HttpClient client; // <2>

    @Test
    void addBookClientApiTest() {
        var body = new BookInfo("Building Microservices", BookAvailability.AVAILABLE);
        body.setAuthor("Sam Newman");
        body.setIsbn("9781492034025");
        var response = client.toBlocking()
                .exchange(HttpRequest.POST("/add", body)); // <3>
        assertEquals(HttpStatus.OK, response.status()); // <4>
    }

    @Test
    void searchClientApiTest() {
        var response = client.toBlocking()
                .exchange(HttpRequest.GET(UriBuilder.of("/search")
                        .queryParam("book-name", "Guide")
                        .build()
                ), Argument.listOf(BookInfo.class)); // <5>
        var body = response.body(); // <6>
        assertEquals(HttpStatus.OK, response.status());
        assertEquals(2, body.size()); // <7>
    }
}
