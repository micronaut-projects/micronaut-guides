package example.micronaut;

import example.micronaut.domain.Genre;
import example.micronaut.genre.GenreSaveCommand;
import example.micronaut.genre.GenreUpdateCommand;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.http.uri.UriTemplate;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest // <1>
public class GenreControllerTest {

    @Inject
    @Client("/")
    public RxHttpClient rxHttpClient; // <2>

    BlockingHttpClient getClient() {
        return rxHttpClient.toBlocking();
    }

    @Test
    public void supplyAnInvalidOrderTriggersValidationFailure() {
        assertThrows(HttpClientResponseException.class, () -> {
            List<Genre> genres = getClient().retrieve(HttpRequest.GET("/genres/list?order=foo"), Argument.of(List.class, Genre.class));

            assertNotNull(genres);
            assertEquals(0, genres.size());
        });
    }

    @Test
    public void testFindNonExistingGenreReturns404() {
        assertThrows(HttpClientResponseException.class, () -> {
            Genre genre = getClient().retrieve(HttpRequest.GET("/genres/99"), Argument.of(Genre.class));

            assertNull(genre);
        });
    }

    private HttpResponse saveGenre(String genre) {
        HttpRequest request = HttpRequest.POST("/genres", new GenreSaveCommand(genre)); // <3>
        return getClient().exchange(request);
    }

    @Test
    public void testGenreCrudOperations() {
        List<Long> genreIds = new ArrayList<>();
        HttpResponse response = saveGenre("DevOps");
        genreIds.add(entityId(response));
        assertEquals(HttpStatus.CREATED, response.getStatus());

        response = saveGenre("Microservices"); // <3>
        assertEquals(HttpStatus.CREATED, response.getStatus());

        Long id = entityId(response);
        genreIds.add(id);
        Genre genre = show(id);
        assertEquals("Microservices", genre.getName());

        response = update(id, "Micro-services");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatus());

        genre = show(id);
        assertEquals("Micro-services", genre.getName());

        List<Genre> genres = listGenres(ListingArguments.builder().build());
        assertEquals(2, genres.size());

        genres = listGenres(ListingArguments.builder().max(1).build());
        assertEquals(1, genres.size());
        assertEquals("DevOps", genres.get(0).getName());

        genres = listGenres(ListingArguments.builder().max(1).order("desc").sort("name").build());
        assertEquals(1, genres.size());
        assertEquals("Micro-services", genres.get(0).getName());

        genres = listGenres(ListingArguments.builder().max(1).offset(10).build());
        assertEquals(0, genres.size());

        // cleanup:
        for (Long genreId : genreIds) {
            response = delete(genreId);
            assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
        }
    }

    private List<Genre> listGenres(ListingArguments args) {
        URI uri = args.of(UriBuilder.of("/genres/list"));
        HttpRequest request = HttpRequest.GET(uri);
        return getClient().retrieve(request, Argument.of(List.class, Genre.class)); // <4>
    }

    private Genre show(Long id) {
        String uri = UriTemplate.of("/genres/{id}").expand(Collections.singletonMap("id", id));
        HttpRequest request = HttpRequest.GET(uri);
        return getClient().retrieve(request, Genre.class);
    }

    private HttpResponse update(Long id, String name) {
        HttpRequest request = HttpRequest.PUT("/genres", new GenreUpdateCommand(id, name));
        return getClient().exchange(request); // <5>
    }

    private HttpResponse delete(Long id) {
        HttpRequest request = HttpRequest.DELETE("/genres/" + id);
        return getClient().exchange(request);
    }

    protected Long entityId(HttpResponse response) {
        String path = "/genres/";
        String value = response.header(HttpHeaders.LOCATION);
        if (value == null) {
            return null;
        }
        int index = value.indexOf(path);
        if (index != -1) {
            return Long.valueOf(value.substring(index + path.length()));
        }
        return null;
    }
}
