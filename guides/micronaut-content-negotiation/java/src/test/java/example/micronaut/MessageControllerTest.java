package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest // <1>
class MessageControllerTest {

    @Test
    void contentNegotiation(@Client("/") HttpClient httpClient) { // <2>
        BlockingHttpClient client = httpClient.toBlocking();
        String expected = """
                {"message":"Hello World"}""";
        String json = assertDoesNotThrow(() -> client.retrieve(HttpRequest.GET("/")
                .accept(MediaType.APPLICATION_JSON))); // <3>
        assertEquals(expected, json);
        String html = assertDoesNotThrow(() -> client.retrieve(HttpRequest.GET("/")
                .accept(MediaType.TEXT_HTML)));
        assertTrue(html.contains("<!DOCTYPE html>"));
        assertTrue(html.contains("<h1>Hello World</h1>"));
    }
}