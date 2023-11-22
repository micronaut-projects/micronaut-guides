package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.client.annotation.Client;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;

@Client("/fruits")
interface FruitClient {

    @Get
    Iterable<Fruit> list();

    @Get("/{name}")
    Optional<Fruit> find(@NonNull @NotBlank @PathVariable String name);

    @Post
    HttpResponse<Fruit> create(@NonNull @NotNull @Valid @Body FruitCommand fruit);

    @Put
    Optional<Fruit> update(@NonNull @NotNull @Valid @Body FruitCommand fruit);

    @NonNull
    @Delete
    HttpStatus delete(@NonNull @Valid @Body FruitCommand fruit);
}
