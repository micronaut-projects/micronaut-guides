package example.micronaut

import io.micronaut.context.annotation.Requires
import io.micronaut.retry.annotation.Fallback
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux

import static io.micronaut.context.env.Environment.TEST

@Requires(env = TEST)
@Fallback
@Singleton
class BookCatalogueClientStub implements BookCatalogueOperations {

    @Override
    Publisher<Book> findAll() {
        Book buildingMicroservices = new Book("1491950358", "Building Microservices")
        Book releaseIt = new Book("1680502395", "Release It!")
        Flux.just(buildingMicroservices, releaseIt)
    }
}
