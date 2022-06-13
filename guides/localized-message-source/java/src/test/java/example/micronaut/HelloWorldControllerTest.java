package example.micronaut;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest // <1>
class HelloWorldControllerTest {

    @Inject
    @Client("/") // <2>
    HttpClient httpClient;

    @Test
    void useOfLocalizedMessageSource() {
        BlockingHttpClient client = httpClient.toBlocking();
        HttpRequest<?> request = HttpRequest.GET("/")
                .header(HttpHeaders.ACCEPT_LANGUAGE, "es"); // <3>
        assertEquals("Hola Mundo", client.retrieve(request));
        request = HttpRequest.GET("/");
        assertEquals("Hello World", client.retrieve(request));
    }

}