package example.micronaut

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client

import javax.validation.constraints.NotNull

@Client("/fruits")
interface FruitClient {

    @Get
    Iterable<Fruit> list()

    @Get("/{id}")
    Optional<Fruit> find(@PathVariable String id)

    @Get("/q")
    Iterable<Fruit> query(@QueryValue @NotNull List<String> names)

    @Post
    HttpResponse<Fruit> save(Fruit fruit)

    @Put
    Fruit update(Fruit fruit)
}
