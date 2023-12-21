package example.micronaut;
/*
//tag::package[]
package example.micronaut;
//end::package[]
*/
//tag::imports[]

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.retry.annotation.Recoverable;
import org.reactivestreams.Publisher;
//end::imports[]

//tag::harcoded[]
@Client("http://localhost:8081") // <1>
@Recoverable(api = BookCatalogueOperations.class)
//end::harcoded[]
//tag::clazz[]
public interface BookCatalogueClient extends BookCatalogueOperations {

    @Get("/books")
    Publisher<Book> findAll();
}
//end::clazz[]
