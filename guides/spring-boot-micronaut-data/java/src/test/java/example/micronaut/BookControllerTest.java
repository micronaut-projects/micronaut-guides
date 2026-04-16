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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestClient;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // <1>
class BookControllerTest {
    @LocalServerPort // <2>
    private int port;

    private RestClient restClient;

    @Autowired // <3>
    BookRepository bookRepository;

    @BeforeEach
    void setup() {
        restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    void booksGet() {
        assertEquals(0, booksJsonArray().length);
        Book moreJava17 = bookRepository.save("More Java 17", 951);
        Book[] books = booksJsonArray();
        assertEquals(1, books.length);
        assertNotNull(books[0].id());
        assertEquals(books[0].pages(), 951);
        assertEquals(books[0].title(), "More Java 17");
        bookRepository.delete(moreJava17);
        assertEquals(0, booksJsonArray().length);
    }

    private Book[] booksJsonArray() {
        return restClient.get()
                .uri("/books")
                .retrieve()
                .body(Book[].class);
    }

}
