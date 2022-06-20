package example.micronaut;

import org.reactivestreams.Publisher;

public interface BookCatalogueOperations {
    Publisher<Book> findAll();
}
