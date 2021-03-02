package example.micronaut

import io.reactivex.Flowable

interface BookCatalogueOperations {
    fun findAll(): Flowable<Book>
}
