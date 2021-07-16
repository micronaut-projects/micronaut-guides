package example.micronaut

import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment
import io.micronaut.retry.annotation.Fallback
import reactor.core.publisher.Flux
import org.reactivestreams.Publisher
import jakarta.inject.Singleton

@Requires(env = Environment.TEST)
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
