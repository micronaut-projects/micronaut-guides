package example.micronaut;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

@Client(id = "quoters")
public interface QuotersClient {
    @Get("/api/random")
    Quote random();
}
