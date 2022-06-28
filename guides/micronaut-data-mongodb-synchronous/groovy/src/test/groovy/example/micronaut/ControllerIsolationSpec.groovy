package example.micronaut

import io.micronaut.context.annotation.Property
import io.micronaut.context.annotation.Replaces
import io.micronaut.context.annotation.Requires
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.inject.Singleton
import spock.lang.Specification

@MicronautTest
@Property(name = "spec.name", value = "controller-isolation")
class ControllerIsolationSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient httpClient

    void checkSerialization() {
        when:
        def get = HttpRequest.GET('/fruits')
        HttpResponse<List<Fruit>> response = httpClient.toBlocking().exchange(get, Argument.listOf(Fruit.class))

        then:
        response.status == HttpStatus.OK
        response.headers.get(HttpHeaders.CONTENT_TYPE) == MediaType.APPLICATION_JSON
        response.getBody().present

        response.body().collect { [it.name, it.description]} == [ ["apple", "red"], ["banana", "yellow"] ]
    }

    @Singleton
    @Replaces(DefaultFruitService.class)
    @Requires(property = "spec.name", value = "controller-isolation")
    static class MockService implements FruitService {

        @Override
        Iterable<Fruit> list() {
            [
                    new Fruit("apple", "red"),
                    new Fruit("banana", "yellow")
            ]
        }

        @Override
        Fruit save(Fruit fruit) {
            fruit
        }

        @Override
        Optional<Fruit> find(@NonNull String id) {
            Optional.empty()
        }

        @Override
        Iterable<Fruit> findByNameInList(List<String> name) {
            []
        }
    }
}
