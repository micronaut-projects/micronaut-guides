package example.micronaut

import reactor.core.publisher.Flux
import org.reactivestreams.Publisher

interface BookCatalogueOperations {
    Publisher<Book> findAll()
}
