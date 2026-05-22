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

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.web.client.RestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // <1>
class BookControllerTest {

    @LocalServerPort // <2>
    private var port = 0

    private lateinit var restClient: RestClient

    @Autowired // <3>
    private lateinit var bookRepository: BookRepository

    @BeforeEach
    fun setup() {
        restClient = RestClient.builder()
            .baseUrl("http://localhost:$port")
            .build()
    }

    @Test
    fun booksGet() {
        assertEquals(0, booksJsonArray().size)
        val moreKotlin = bookRepository.save("More Kotlin", 951)
        val books = booksJsonArray()
        assertEquals(1, books.size)
        assertNotNull(books[0].id)
        assertEquals(951, books[0].pages)
        assertEquals("More Kotlin", books[0].title)
        bookRepository.delete(moreKotlin)
        assertEquals(0, booksJsonArray().size)
    }

    private fun booksJsonArray(): Array<Book> =
        restClient.get()
            .uri("/books")
            .retrieve()
            .body(Array<Book>::class.java) ?: emptyArray()
}
