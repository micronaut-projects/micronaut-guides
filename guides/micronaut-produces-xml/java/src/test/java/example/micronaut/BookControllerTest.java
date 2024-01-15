package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
@MicronautTest // <1>
class BookControllerTest {
    @Test
    void testXmlRendered(@Client("/") HttpClient httpClient) { // <2>
        BlockingHttpClient client = httpClient.toBlocking();
        String xml = assertDoesNotThrow(() ->
                client.retrieve(HttpRequest.GET("/book").accept(MediaType.APPLICATION_XML), String.class));  // <3>
        assertEquals("""
                <book isbn="1491950358"><name>Building Microservices</name></book>""", xml);
    }
}