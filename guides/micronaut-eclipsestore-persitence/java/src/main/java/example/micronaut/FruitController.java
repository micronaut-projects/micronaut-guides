/*
 * Copyright 2017-2023 original authors
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
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.Status;
import io.micronaut.scheduling.annotation.ExecuteOn;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Collection;

import static io.micronaut.scheduling.TaskExecutors.BLOCKING;

@Controller("/fruits") // <1>
class FruitController {

    private final FruitRepository fruitRepository;

    FruitController(FruitRepository fruitRepository) {  // <2>
        this.fruitRepository = fruitRepository;
    }

    @Get // <3>
    Collection<Fruit> list() {
        return fruitRepository.list();
    }

    @ExecuteOn(BLOCKING)
    @Post // <4>
    @Status(HttpStatus.CREATED) // <5>
    Fruit create(@NonNull @NotNull @Valid @Body FruitCommand fruit) { // <6>
        return fruitRepository.create(fruit);
    }

    @ExecuteOn(BLOCKING)
    @Put
    Fruit update(@NonNull @NotNull @Valid @Body FruitCommand fruit) {
        return fruitRepository.update(fruit);
    }

    @Get("/{name}") // <7>
    Fruit find(@PathVariable String name) {
        return fruitRepository.find(name);
    }

    @ExecuteOn(BLOCKING)
    @Delete
    @Status(HttpStatus.NO_CONTENT)
    void delete(@NonNull @Valid @Body FruitCommand fruit) {
        fruitRepository.delete(fruit);
    }
}
