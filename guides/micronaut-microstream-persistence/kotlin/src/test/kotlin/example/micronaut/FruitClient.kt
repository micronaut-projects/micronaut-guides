package example.micronaut

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.client.annotation.Client
import java.util.Optional

@Client("/fruits")
interface FruitClient {

    @Get
    fun list(): Iterable<Fruit>

    @Get("/{name}")
    fun find(@PathVariable name: String?): Optional<Fruit>

    @Post
    fun create(fruit: FruitCommand?): HttpResponse<Fruit>

    @Put
    fun update(fruit: FruitCommand): Fruit?
}