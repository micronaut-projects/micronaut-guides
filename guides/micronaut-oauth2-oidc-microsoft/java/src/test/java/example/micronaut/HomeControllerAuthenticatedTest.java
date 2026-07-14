package example.micronaut;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.filters.AuthenticationFetcher;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;

import java.security.PublicKey;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Property(name = "spec.name", value = "HomeControllerAuthenticatedTest")
@MicronautTest
class HomeControllerAuthenticatedTest {

    @Test
    void homeRendersHtmlPage(@Client("/")HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        String html = assertDoesNotThrow(() -> client.retrieve(HttpRequest.GET("/").accept(MediaType.TEXT_HTML)));
        assertTrue(html.contains("<!DOCTYPE html>"));
        assertFalse(html.contains("Anonymous"));
        assertTrue(html.contains("sergio.delamo@softamo.com"));
    }

    @Requires(property = "spec.name", value = "HomeControllerAuthenticatedTest")
    @Singleton
    static class MockAuthenticationFetcher implements AuthenticationFetcher<HttpRequest<?>> {

        @Override
        public Publisher<Authentication> fetchAuthentication(HttpRequest<?> request) {
            return Publishers.just(Authentication.build("sdelamo", Map.of("email", "sergio.delamo@softamo.com")));
        }
    }
}