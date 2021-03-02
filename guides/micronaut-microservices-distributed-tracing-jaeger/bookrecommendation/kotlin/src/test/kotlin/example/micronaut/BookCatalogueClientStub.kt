package example.micronaut

import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment
import io.micronaut.retry.annotation.Fallback
import io.reactivex.Flowable

import javax.inject.Singleton

@Requires(env = arrayOf(Environment.TEST))
@Fallback
@Singleton
class BookCatalogueClientStub : BookCatalogueOperations {

    override fun findAll(): Flowable<Book> {
        val buildingMicroservices = Book("1491950358", "Building Microservices")
        val releaseIt = Book("1680502395", "Release It!")
        return Flowable.just(buildingMicroservices, releaseIt)
    }
}
