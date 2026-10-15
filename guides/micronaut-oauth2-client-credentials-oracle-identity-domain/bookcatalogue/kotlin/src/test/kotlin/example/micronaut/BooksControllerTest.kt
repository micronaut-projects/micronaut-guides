/*
 * Copyright 2017-2025 original authors
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

import io.micronaut.context.annotation.Property
import io.micronaut.core.type.Argument
import io.micronaut.core.util.StringUtils
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@Property(name = "micronaut.security.enabled", value = StringUtils.FALSE)
@MicronautTest // <1>
class BooksControllerTest(@Client("/") val client: HttpClient) { // <2>

    @Test
    fun testRetrieveBooks() {
        val request: HttpRequest<*> = HttpRequest.GET<Any>("/books") // <3>
        val books = client.toBlocking().retrieve(request, Argument.listOf(Book::class.java)) // <4>

        assertEquals(3, books.size)
        assertTrue(books.contains(Book("1491950358", "Building Microservices")))
        assertTrue(books.contains(Book("1680502395", "Release It!")))
    }
}
