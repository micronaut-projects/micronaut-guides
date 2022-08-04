//tag::packageandimports[]
package example.micronaut;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.retry.annotation.Recoverable;
import org.reactivestreams.Publisher;
//end::packageandimports[]

//tag::harcoded[]
@Client(id = "bookcatalogue") // <1>
@Recoverable(api = BookCatalogueOperations.class)
//end::harcoded[]
//tag::clazz[]
public interface BookCatalogueClient extends BookCatalogueOperations {

    @Get("/books")
    Publisher<Book> findAll();
}
//end::clazz[]
