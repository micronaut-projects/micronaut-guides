package example.micronaut.controllers;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Property(name = "micronaut.security.reject-not-found", value = StringUtils.FALSE)
@MicronautTest
class LoginControllerTest {

    @Test
    void auth(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        HttpRequest<?> request = HttpRequest.GET("/user/auth").accept(MediaType.TEXT_HTML);
        String html = assertDoesNotThrow(() -> client.retrieve(request));
        assertNotNull(html);
    }

    @Test
    void authFailed(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        HttpRequest<?> request = HttpRequest.GET("/user/authFailed").accept(MediaType.TEXT_HTML);
        String html = assertDoesNotThrow(() -> client.retrieve(request));
        assertNotNull(html);
    }
}