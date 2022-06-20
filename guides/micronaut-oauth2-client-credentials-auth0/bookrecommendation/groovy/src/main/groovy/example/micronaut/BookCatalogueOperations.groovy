package example.micronaut

import org.reactivestreams.Publisher

interface BookCatalogueOperations {
    Publisher<Book> findAll()
}
