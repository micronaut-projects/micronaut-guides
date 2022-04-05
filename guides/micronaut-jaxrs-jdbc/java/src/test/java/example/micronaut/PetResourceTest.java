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

import static org.junit.jupiter.api.Assertions.*;

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
        assertEquals(HttpStatus.NOT_FOUND , thrown.getResponse().status()); // <3>

        repository.deleteById(id);
    }
}
