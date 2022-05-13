package example.micronaut

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.client.annotation.Client

@Client("/fruits")
interface FruitClient {

    @Get
    Iterable<Fruit> list()

    @Get("/{name}")
    Optional<Fruit> find(@PathVariable String name)

    @Post
    HttpResponse<Fruit> create(FruitCommand fruit)

    @Put
    Fruit update(FruitCommand fruit)
}
