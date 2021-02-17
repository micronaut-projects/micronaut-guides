package example.micronaut

import io.reactivex.Flowable

interface BookCatalogueOperations {
    Flowable<Book> findAll()
}
