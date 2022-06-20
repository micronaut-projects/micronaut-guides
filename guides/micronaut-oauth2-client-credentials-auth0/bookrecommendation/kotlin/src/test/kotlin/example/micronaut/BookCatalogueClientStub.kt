package example.micronaut

import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment.TEST
import io.micronaut.retry.annotation.Fallback
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux

@Requires(env = [TEST])
@Fallback
@Singleton
class BookCatalogueClientStub : BookCatalogueOperations {

    override fun findAll(): Publisher<Book> {
        val buildingMicroservices = Book("1491950358", "Building Microservices")
        val releaseIt = Book("1680502395", "Release It!")
        return Flux.just(buildingMicroservices, releaseIt)
    }
}
