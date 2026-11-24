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
import io.micronaut.http.HttpRequest
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Singleton
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

@Property(name = "spec.name", value = "ContactControllerTest") // <1>
@MicronautTest // <2>
class ContactControllerTest {

    @Test
    fun contactsAreRenderedAsHtml(@Client("/") httpClient: HttpClient) { // <3>
        val client = httpClient.toBlocking()
        val request = HttpRequest.GET<Any>("/contacts").accept(MediaType.TEXT_HTML) // <4>
        val html = assertDoesNotThrow { client.retrieve(request, String::class.java) } // <5>
        assertTrue(html.contains("<!DOCTYPE html>"))
        assertTrue(html.contains("Katherine"))
        assertTrue(html.contains("Adams"))
        assertTrue(html.contains("Tim"))
        assertTrue(html.contains("Cook"))
    }

    @Requires(property = "spec.name", value = "ContactControllerTest") // <1>
    @Replaces(ContactRepository::class) // <6>
    @Singleton
    open class ContactRepositoryReplacement : ContactRepository {
        private val contacts = listOf(
            Contact("Katherine", "Adams", "Apple Inc."),
            Contact("Tim", "Cook", "Apple Inc."))

        override fun findAll(): List<Contact> = contacts

        override fun findByFirstName(firstName: String?): Contact? =
            contacts.firstOrNull { it.firstName == firstName }
    }
}
