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

import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import jakarta.inject.Inject;
import org.junit.jupiter.api.function.Executable;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest(transactional = false) // <1>
public class PetResourceTest {

    @Inject
    @Client("/")
    HttpClient httpClient; // <2>

    @Inject
    PetRepository repository;

    @Test
    void testAll() {
        Pet dino = new Pet("Dino");
        Pet bp = new Pet("Baby Puss");
        bp.setType(PetType.CAT);
        Pet hoppy = new Pet("Hoppy");

        repository.saveAll(Arrays.asList(dino, bp, hoppy));

        HttpResponse<List<NameDto>> response = httpClient.toBlocking()
                .exchange(HttpRequest.GET("/pets"), Argument.listOf(NameDto.class));
        assertEquals(HttpStatus.OK , response.status());
        List<NameDto> petNames = response.body();
        assertNotNull(petNames);
        assertEquals(3, petNames.size());
        repository.deleteAll();
    }

    @Test
    void testGet() {
        String name = "Dino";
        Pet dino = new Pet(name);
        Long id = repository.save(dino).getId();

        URI uri = UriBuilder.of("/pets").path(name).build();
        HttpResponse<Pet> response = httpClient.toBlocking()
                .exchange(HttpRequest.GET(uri), Pet.class);
        assertEquals(HttpStatus.OK , response.status());
        Pet pet = response.body();
        assertNotNull(pet);
        assertNotNull(pet.getId());
        assertNotNull(pet.getType());
        assertEquals(PetType.DOG, pet.getType());
        assertEquals(name, pet.getName());
        repository.deleteById(id);
    }

    @Test
    void testGetIfPetDoesNotExistsAPIReturnsNotFound() {
        String name = "Dino";
        Pet dino = new Pet(name);
        Long id = repository.save(dino).getId();

        URI uri = UriBuilder.of("/pets").path("Foo").build();
        Executable e = () -> httpClient.toBlocking()
                .exchange(HttpRequest.GET(uri), Pet.class);
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, e);
        assertEquals(HttpStatus.NOT_FOUND , thrown.getResponse().status()); // <2>

        repository.deleteById(id);
    }

    @Test
    void testSave() {
        String name = "Dino";
        long oldCount = repository.count();
        PetSave petSave = new PetSave(name, PetType.DOG);
        HttpRequest<?> request = HttpRequest.POST("/pets", petSave);
        HttpResponse<Pet> response = httpClient.toBlocking()
                .exchange(request, Pet.class);
        assertEquals(HttpStatus.CREATED , response.status());
        long count = repository.count();
        assertEquals(oldCount + 1, count);
        repository.deleteAll();
    }
}
