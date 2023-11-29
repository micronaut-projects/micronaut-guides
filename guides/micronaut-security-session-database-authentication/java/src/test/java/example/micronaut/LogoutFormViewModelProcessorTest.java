package example.micronaut;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.utils.SecurityService;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Property(name = "spec.name", value = "LogoutFormViewModelProcessorTest")
@MicronautTest
class LogoutFormViewModelProcessorTest {

    @Test
    void logoutForm(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        String html = assertDoesNotThrow(() -> client.retrieve(HttpRequest.GET("/").accept(MediaType.TEXT_HTML)));
        assertNotNull(html);
        assertTrue(html.contains("action=\"/logout\""));
    }

    @Requires(property = "spec.name", value = "LogoutFormViewModelProcessorTest")
    @Replaces(SecurityService.class)
    @Singleton
    static class SecurityServiceReplacement implements SecurityService {
        @Override
        public boolean isAuthenticated() {
            return username().isPresent();
        }

        @Override
        public boolean hasRole(String role) {
            return false;
        }

        @Override
        public Optional<Authentication> getAuthentication() {
            return username().map(Authentication::build);
        }

        @Override
        public Optional<String> username() {
            return Optional.of("admin");
        }
    }
}