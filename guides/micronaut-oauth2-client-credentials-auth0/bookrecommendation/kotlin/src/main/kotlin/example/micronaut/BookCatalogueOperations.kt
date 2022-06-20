package example.micronaut

import org.reactivestreams.Publisher

interface BookCatalogueOperations {
    fun findAll(): Publisher<Book>
}
