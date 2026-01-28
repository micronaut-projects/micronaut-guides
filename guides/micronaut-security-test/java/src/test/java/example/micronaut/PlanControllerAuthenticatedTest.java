package example.micronaut;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.filters.AuthenticationFetcher;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Property(name = "spec.name", value = "PlanControllerAuthenticatedTest") // <1>
@MicronautTest // <2>
class PlanControllerAuthenticatedTest {

    @Test
    void planControllerRequiresAuthentication(@Client("/") HttpClient httpClient) { // <3>
        BlockingHttpClient client = httpClient.toBlocking();
        var response = assertDoesNotThrow(
                () -> client.retrieve(HttpRequest.GET("/plan").accept(MediaType.TEXT_PLAIN)));
        assertEquals("Plan New Year", response);
    }

    @Requires(property = "spec.name", value = "PlanControllerAuthenticatedTest") // <1>
    @Singleton
    static class MockAuthenticationFetcher implements AuthenticationFetcher<HttpRequest<?>> {

        @Override
        public Publisher<Authentication> fetchAuthentication(HttpRequest<?> request) {
            return Publishers.just(Authentication.build("watson"));
        }
    }
}