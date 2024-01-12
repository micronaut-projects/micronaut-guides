/*
 * Copyright 2017-2024 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
