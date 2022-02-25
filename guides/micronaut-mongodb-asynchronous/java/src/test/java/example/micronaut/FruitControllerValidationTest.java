package example.micronaut;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import static io.micronaut.http.HttpStatus.BAD_REQUEST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Property(name = "spec.name", value = "FruitControllerValidationTest")
@MicronautTest
public class FruitControllerValidationTest {

    @Inject
    @Client("/")
    HttpClient httpClient;

    @Test
    public void testFruitIsValidated() {
        HttpClientResponseException e = assertThrows(HttpClientResponseException.class, () ->
                httpClient.toBlocking().exchange(HttpRequest.POST("/fruits", new Fruit("", "Hola"))));
        assertEquals(BAD_REQUEST, e.getStatus());
    }

    @Requires(property = "spec.name", value = "FruitControllerValidationTest")
    @Singleton
    @Replaces(FruitRepository.class)
    static class MockFruitRepository implements FruitRepository {

        @Override
        public Publisher<Fruit> list() {
            return Publishers.empty();
        }

        @Override
        public Mono<Boolean> save(Fruit fruit) {
            return Mono.just(false);
        }
    }
}
