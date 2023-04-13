package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.type.Argument;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@MicronautTest // <1>
@TestInstance(PER_CLASS) // <2>
class BooksControllerTest { // <3>

    @Inject
    @Client("/")
    HttpClient httpClient; // <4>

    private static HttpRequest<?> saveRequest(String isbn, String name) {
        return HttpRequest.POST("/books",
                CollectionUtils.mapOf("isbn", isbn, "name", name));
    }

    @Test
    void testRetrieveBooks() {
        BlockingHttpClient client = httpClient.toBlocking();

        String releaseItIsbn = "1680502395";
        String releaseItName = "Release It!";
        HttpResponse<?> saveResponse = client.exchange(
                saveRequest(releaseItIsbn, releaseItName));
        assertEquals(HttpStatus.CREATED, saveResponse.status());
        String location = saveResponse.getHeaders().get(HttpHeaders.LOCATION);
        assertNotNull(location);
        assertTrue(location.startsWith("/books/"));
        String releaseItId = location.substring("/books/".length());

        String continuousDeliveryIsbn = "0321601912";
        String continuousDeliveryName = "Continuous Delivery";
        saveResponse = client.exchange(
                saveRequest(continuousDeliveryIsbn, continuousDeliveryName));
        assertEquals(HttpStatus.CREATED, saveResponse.status());
        location = saveResponse.getHeaders().get(HttpHeaders.LOCATION);
        assertNotNull(location);
        assertTrue(location.startsWith("/books/"));
        String continuousDeliveryId = location.substring("/books/".length());

        String buildingMicroservicesIsbn = "1491950358";
        String buildingMicroservicesName = "Building Microservices";
        saveResponse = client.exchange(
                saveRequest(buildingMicroservicesIsbn, buildingMicroservicesName));
        assertEquals(HttpStatus.CREATED, saveResponse.status());
        location = saveResponse.getHeaders().get(HttpHeaders.LOCATION);
        assertNotNull(location);
        assertTrue(location.startsWith("/books/"));
        String buildingMicroservicesId = location.substring("/books/".length());

        Book result = client.retrieve(
                HttpRequest.GET(UriBuilder.of("/books")
                        .path(continuousDeliveryId)
                        .build()), Book.class);
        assertEquals(continuousDeliveryName, result.getName());

        List<Book> books = client.retrieve(HttpRequest.GET("/books"),
                Argument.listOf(Book.class));
        assertEquals(3, books.size());

        assertTrue(books.stream().anyMatch(it ->
                it.getIsbn().equals(continuousDeliveryIsbn) &&
                        it.getName().equals(continuousDeliveryName)));
        assertTrue(books.stream().anyMatch(it ->
                it.getIsbn().equals(releaseItIsbn) &&
                        it.getName().equals(releaseItName)));
        assertTrue(books.stream().anyMatch(it ->
                it.getIsbn().equals(buildingMicroservicesIsbn) &&
                        it.getName().equals(buildingMicroservicesName)));

        HttpResponse<?> deleteResponse = client.exchange(
                HttpRequest.DELETE(UriBuilder.of("/books")
                        .path(continuousDeliveryId)
                        .build().toString()));
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatus());
        deleteResponse = client.exchange(HttpRequest.DELETE(UriBuilder.of("/books")
                .path(releaseItId)
                .build().toString()));
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatus());
        deleteResponse = client.exchange(HttpRequest.DELETE(UriBuilder.of("/books")
                .path(buildingMicroservicesId)
                .build().toString()));
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatus());
    }
}
