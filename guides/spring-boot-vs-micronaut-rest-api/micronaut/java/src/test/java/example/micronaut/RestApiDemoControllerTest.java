package example.micronaut;

import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import jakarta.inject.Inject;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class RestApiDemoControllerTest {
    @Test
    void findAllTest(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        HttpRequest<?> request = HttpRequest.GET("/coffees");
        List<Coffee> coffeeList = assertDoesNotThrow(() -> client.retrieve(request, Argument.listOf(Coffee.class)));
        assertNotNull(coffeeList);
        assertEquals(4, coffeeList.size());
    }

    @Test
    void findByIdTest(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        HttpRequest<?> request = HttpRequest.GET(UriBuilder.of("/coffees").path("99").build());
        HttpClientResponseException ex = assertThrows(HttpClientResponseException.class, () -> client.retrieve(request));
        assertNotNull(ex);
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());


        List<Coffee> coffeeList = assertDoesNotThrow(() -> client.retrieve(HttpRequest.GET("/coffees"), Argument.listOf(Coffee.class)));
        assertNotNull(coffeeList);
        assertEquals(4, coffeeList.size());

        HttpRequest<?> findByIdRequest = HttpRequest.GET(UriBuilder.of("/coffees")
                .path(coffeeList.get(0).getId())
                .build());

        HttpResponse<Coffee> coffee = assertDoesNotThrow(() -> client.exchange(findByIdRequest, Coffee.class));
        assertNotNull(coffee);
        assertTrue(coffee.getBody().isPresent());
        assertEquals(coffeeList.get(0).getName(), coffee.getBody().get().getName());
    }

    @Test
    void postTest(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        HttpRequest<?> request = HttpRequest.POST("/coffees", new Coffee("Café Olé"));
        HttpResponse<Coffee> coffeeHttpResponse = assertDoesNotThrow(() -> client.exchange(request, Coffee.class));
        assertNotNull(coffeeHttpResponse);
        assertEquals(HttpStatus.OK, coffeeHttpResponse.getStatus());
        assertTrue(coffeeHttpResponse.getBody().isPresent());
        Coffee coffee = coffeeHttpResponse.getBody().get();
        assertEquals("Café Olé", coffee.getName());

        List<Coffee> coffeeList = assertDoesNotThrow(() -> client.retrieve(HttpRequest.GET("/coffees"), Argument.listOf(Coffee.class)));
        assertNotNull(coffeeList);
        assertEquals(5, coffeeList.size());

        HttpRequest<?> deleteRequest = HttpRequest.DELETE(UriBuilder.of("/coffees")
                .path(coffee.getId())
                .build());

        HttpResponse<?> deleteResponse = assertDoesNotThrow(() -> client.exchange(deleteRequest));
        assertNotNull(deleteResponse);
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatus());

        coffeeList = assertDoesNotThrow(() -> client.retrieve(HttpRequest.GET("/coffees"), Argument.listOf(Coffee.class)));
        assertNotNull(coffeeList);
        assertEquals(4, coffeeList.size());
    }

    @Test
    void putTest(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        String id  = UUID.randomUUID().toString();
        URI path = UriBuilder.of("/coffees").path(id).build();
        HttpRequest<?> request = HttpRequest.PUT(path, new Coffee(id, "Café Astro"));
        HttpResponse<Coffee> coffeeHttpResponse = assertDoesNotThrow(() -> client.exchange(request, Coffee.class));
        assertNotNull(coffeeHttpResponse);
        assertEquals(HttpStatus.CREATED, coffeeHttpResponse.getStatus());
        assertTrue(coffeeHttpResponse.getBody().isPresent());
        Coffee coffee = coffeeHttpResponse.getBody().get();
        assertEquals("Café Astro", coffee.getName());

        List<Coffee> coffeeList = assertDoesNotThrow(() -> client.retrieve(HttpRequest.GET("/coffees"), Argument.listOf(Coffee.class)));
        assertNotNull(coffeeList);
        assertEquals(5, coffeeList.size());

        HttpRequest<?> updateRequest = HttpRequest.PUT(path, new Coffee(coffee.getId(), "Café Alien"));
        coffeeHttpResponse = assertDoesNotThrow(() -> client.exchange(updateRequest, Coffee.class));
        assertNotNull(coffeeHttpResponse);
        assertEquals(HttpStatus.OK, coffeeHttpResponse.getStatus());
        assertTrue(coffeeHttpResponse.getBody().isPresent());
        coffee = coffeeHttpResponse.getBody().get();
        assertEquals("Café Alien", coffee.getName());

        coffeeList = assertDoesNotThrow(() -> client.retrieve(HttpRequest.GET("/coffees"), Argument.listOf(Coffee.class)));
        assertNotNull(coffeeList);
        assertEquals(5, coffeeList.size());

        HttpRequest<?> deleteRequest = HttpRequest.DELETE(UriBuilder.of("/coffees")
                .path(coffee.getId())
                .build());

        HttpResponse<?> deleteResponse = assertDoesNotThrow(() -> client.exchange(deleteRequest));
        assertNotNull(deleteResponse);
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatus());

        coffeeList = assertDoesNotThrow(() -> client.retrieve(HttpRequest.GET("/coffees"), Argument.listOf(Coffee.class)));
        assertNotNull(coffeeList);
        assertEquals(4, coffeeList.size());
    }
}
