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

import io.micronaut.context.annotation.Property
import io.micronaut.context.annotation.Replaces
import io.micronaut.context.annotation.Requires
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Singleton
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.Optional

@MicronautTest
@Property(name = "spec.name", value = "controller-isolation")
class ControllerIsolationTest(@param:Client("/") private val httpClient: HttpClient) {

    @Test
    fun checkSerialization() {
        val response = httpClient.toBlocking().exchange(
            HttpRequest.GET<Any>("/fruits"),
            Argument.listOf(Fruit::class.java)
        )

        assertEquals(HttpStatus.OK, response.status)
        assertEquals(MediaType.APPLICATION_JSON, response.headers.get(HttpHeaders.CONTENT_TYPE))

        val all = response.body()!!.joinToString(",") { "${it.name}:${it.description}" }
        assertEquals("apple:red,banana:yellow", all)
    }

    @Singleton
    @Replaces(DefaultFruitService::class)
    @Requires(property = "spec.name", value = "controller-isolation")
    class MockService : FruitService {

        override fun list(): Iterable<Fruit> {
            return listOf(
                Fruit("apple", "red"),
                Fruit("banana", "yellow")
            )
        }

        override fun save(fruit: Fruit): Fruit {
            return fruit
        }

        override fun find(id: String): Optional<Fruit> {
            return Optional.empty()
        }

        override fun findByNameInList(name: List<String>): Iterable<Fruit> {
            return emptyList()
        }
    }
}
