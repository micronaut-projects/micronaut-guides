package example.micronaut

import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment
import io.micronaut.retry.annotation.Fallback
import reactor.core.publisher.Flux
import jakarta.inject.Singleton
import org.reactivestreams.Publisher

@Requires(env = arrayOf(Environment.TEST))
@Fallback
@Singleton
class BookCatalogueClientStub : BookCatalogueOperations {

    override fun findAll(): Publisher<Book> {
        val buildingMicroservices = Book("1491950358", "Building Microservices")
        val releaseIt = Book("1680502395", "Release It!")
        return Flux.just(buildingMicroservices, releaseIt)
    }
}
