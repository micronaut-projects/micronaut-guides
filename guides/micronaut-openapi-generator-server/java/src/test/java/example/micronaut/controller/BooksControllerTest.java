package example.micronaut.controller;

import example.micronaut.model.BookAvailability;
import example.micronaut.model.BookInfo;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.uri.UriTemplate;
import io.micronaut.http.cookie.Cookie;
import io.micronaut.http.client.multipart.MultipartBody;
import io.micronaut.core.type.Argument;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Assertions;
import jakarta.inject.Inject;
import reactor.core.publisher.Mono;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.HashSet;


/**
 * API tests for BooksController
 */
@MicronautTest
public class BooksControllerTest {

    @Inject
    EmbeddedServer server;

    @Inject
    @Client("${context-path}")
    HttpClient client;

    @Inject
    BooksController controller;

    /**
     * This test is used to check that the API available to client through
     * '/add' to the features of addBook() works as desired.
     */
    //tag::addBook[]
    @Test // <1>
    void addBookClientApiTest() {
        // given
        BookInfo body = new BookInfo("My Book", BookAvailability.fromValue("available")); // <2>
        String uri = UriTemplate.of("/add").expand(new HashMap<>());
        MutableHttpRequest<?> request = HttpRequest.POST(uri, body)
                .contentType("application/json")
                .accept("application/json");

        // when
        HttpResponse<?> response = client.toBlocking().exchange(request);

        // then
        Assertions.assertEquals(HttpStatus.OK, response.status());
    }
    //end::addBook[]

    /**
     * This test is used to check that the API available to client through
     * '/search' to the features of search() works as desired.
     */
    //tag::search[]
    @Test // <1>
    void searchClientApiTest() {
        // given
        String uri = UriTemplate.of("/search").expand(new HashMap<>());
        MutableHttpRequest<?> request = HttpRequest.GET(uri)
                .accept("applicaton/json");
        request.getParameters()
                .add("book-name", "Guide"); // <2>

        // when
        HttpResponse<?> response = client.toBlocking().exchange(request, Argument.of(List.class, BookInfo.class));
        List<BookInfo> body = (List<BookInfo>) response.body(); // <3>

        // then
        Assertions.assertEquals(HttpStatus.OK, response.status());
        Assertions.assertEquals(2, body.size()); // <4>
    }
    //end::search[]

}
