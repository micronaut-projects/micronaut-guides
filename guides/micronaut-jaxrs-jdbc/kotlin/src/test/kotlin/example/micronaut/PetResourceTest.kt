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
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.http.uri.UriBuilder
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@MicronautTest(transactional = false) // <1>
class PetResourceTest {

    @Inject
    @field:Client("/")
    lateinit var httpClient : HttpClient // <2>

    @Inject
    lateinit var repository: PetRepository

    @Test
    fun testAll() {
        val dino = Pet("Dino")
        val pb = Pet("Baby Puss", PetType.CAT)
        val hoppy = Pet("Hoppy")

        repository.saveAll(listOf(dino, pb, hoppy))

        val client = httpClient.toBlocking()
        val request: HttpRequest<Any> = HttpRequest.GET("/pets")
        val response = client.exchange<Any, MutableList<NameDto>>(request,
            Argument.listOf(NameDto::class.java))
        assertEquals(HttpStatus.OK, response.status)
        val petNames = response.body()
        assertNotNull(petNames)
        assertEquals(3, petNames.size)
        repository.deleteAll()
    }
    
    @Test
    fun testGet() {
        val name = "Dino"
        val dino = Pet(name)
        val id = repository.save(dino).id
        val uri = UriBuilder.of("/pets").path(name).build()
        val request : HttpRequest<Any> = HttpRequest.GET(uri)
        val client = httpClient.toBlocking()
        val response = client.exchange(request, Pet::class.java)
        assertEquals(HttpStatus.OK, response.status)
        val pet = response.body()
        assertNotNull(pet)
        if (pet != null) {
            assertNotNull(pet.id)
            assertNotNull(pet.type)
            assertEquals(PetType.DOG, pet.type)
            assertEquals(name, pet.name)
        }
        if (id != null) {
            repository.deleteById(id)
        }
    }

    @Test
    fun testGetIfPetDoesNotExistsAPIReturnsNotFound() {
        val name = "Dino"
        val dino = Pet(name)
        val id = repository.save(dino).id
        val uri = UriBuilder.of("/pets").path("Foo").build()
        val client = httpClient.toBlocking()
        val request : HttpRequest<Any> = HttpRequest.GET(uri)
        val thrown: HttpClientResponseException = assertThrows(HttpClientResponseException::class.java) {
            client.exchange(request, Pet::class.java)
        }
        assertEquals(HttpStatus.NOT_FOUND, thrown.response.status()) // <3>
        if (id != null) {
            repository.deleteById(id)
        }
    }

    @Test
    fun testSave() {
        val name = "Dino"
        val oldCount = repository.count()
        val petSave = PetSave(name, PetType.DOG)
        val request: HttpRequest<Any> = HttpRequest.POST("/pets", petSave)
        val client = httpClient.toBlocking()
        val response = client.exchange(request, Pet::class.java)
        assertEquals(HttpStatus.CREATED, response.status())
        val count = repository.count()
        assertEquals(oldCount + 1, count)
        repository.deleteAll()
    }
}