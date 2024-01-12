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
package example.micronaut

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import jakarta.inject.Inject

@MicronautTest
class GraphQLControllerTest(@Client("/") val client: HttpClient) { // <2>

    @Test
    fun testGraphQLController() {
        val query = "{ \"query\": \"{ bookById(id:\\\"book-1\\\") { name, pageCount, author { firstName, lastName} } }\" }"
        val request: HttpRequest<String> = HttpRequest.POST("/graphql", query)

        val rsp = client.toBlocking().exchange(request, Argument.of(Map::class.java))
        assertEquals(HttpStatus.OK, rsp.status())
        assertNotNull(rsp.body())

        val bookInfo = rsp.getBody(Map::class.java).get()["data"] as Map<*, *>
        val bookById = bookInfo["bookById"] as Map<*, *>?
        assertEquals("Harry Potter and the Philosopher's Stone", bookById!!["name"])
        assertEquals(223, bookById["pageCount"])

        val author = bookById["author"] as Map<*, *>?
        assertEquals("Joanne", author!!["firstName"])
        assertEquals("Rowling", author["lastName"])
    }
}
