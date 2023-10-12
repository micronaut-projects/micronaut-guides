package example.micronaut;

import io.micronaut.core.annotation.Blocking;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.client.annotation.Client;

@Client(id = "photos") // <1>
public interface PhotosClient {

    @Get("/photos/{id}")// <2>
    @Blocking
    Photo findById(@PathVariable Long id); // <3>
}
