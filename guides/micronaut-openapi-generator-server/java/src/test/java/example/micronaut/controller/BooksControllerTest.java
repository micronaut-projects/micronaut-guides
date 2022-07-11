package example.micronaut.controller;

import example.micronaut.model.BookAvailability;
import example.micronaut.model.BookInfo;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.core.type.Argument;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import jakarta.inject.Inject;
import java.util.List;

@MicronautTest // <1>
public class BooksControllerTest {

    @Inject
    @Client("${context-path}")
    HttpClient client; // <2>

    @Test
    void addBookClientApiTest() {
        BookInfo body = new BookInfo("Building Microservices", BookAvailability.AVAILABLE);
        body.setAuthor("Sam Newman");
        body.setISBN("9781492034025");
        HttpResponse<?> response = client.toBlocking()
                .exchange(HttpRequest.POST("/add", body)); // <3>
        assertEquals(HttpStatus.OK, response.status()); // <4>
    }

    @Test
    void searchClientApiTest() {
        HttpResponse<List<BookInfo>> response = client.toBlocking()
                .exchange(HttpRequest.GET(UriBuilder.of("/search")
                        .queryParam("book-name", "Guide")
                        .build()
                ), Argument.listOf(BookInfo.class)); // <5>
        List<BookInfo> body = response.body(); // <6>
        assertEquals(HttpStatus.OK, response.status());
        assertEquals(2, body.size()); // <7>
    }
}
