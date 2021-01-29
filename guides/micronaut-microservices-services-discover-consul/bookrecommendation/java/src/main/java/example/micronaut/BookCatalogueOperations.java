package example.micronaut;

import io.reactivex.Flowable;

public interface BookCatalogueOperations {
    Flowable<Book> findAll();
}
