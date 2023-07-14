package example.micronaut

import io.micronaut.core.annotation.NonNull
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client

import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull

@Client('/fruits')
interface FruitClient {

    @Post
    @NonNull
    HttpStatus save(@NonNull @NotNull @Valid Fruit fruit)

    @NonNull
    @Get
    List<Fruit> findAll()
}
