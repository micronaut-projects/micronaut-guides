package example.micronaut

import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client
import jakarta.validation.Valid

@Client("/fruits")
interface FruitClient {

    @Post
    fun save(@Valid fruit: Fruit): HttpStatus

    @Get
    fun findAll(): List<Fruit>
}
