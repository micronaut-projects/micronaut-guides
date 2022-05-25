package example.micronaut

import io.micronaut.context.annotation.Property
import io.micronaut.context.annotation.Replaces
import io.micronaut.context.annotation.Requires
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import jakarta.inject.Singleton
import org.jetbrains.annotations.NotNull
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Property(name = "spec.name", value = "controller-isolation")
class ControllerIsolationSpec extends BaseMongoDataSpec {

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
        Publisher<Fruit> list() {
            Flux.just(new Fruit("apple", "red"), new Fruit("banana", "yellow"))
        }

        @Override
        Publisher<Fruit> save(Fruit fruit) {
            Mono.just(fruit)
        }

        @Override
        Publisher<Fruit> find(@NotNull String id) {
            Mono.empty()
        }

        @Override
        Publisher<Fruit> findByNameInList(List<String> name) {
            Flux.empty()
        }
    }
}
