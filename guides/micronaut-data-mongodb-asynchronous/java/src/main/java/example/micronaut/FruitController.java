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
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.annotation.Status;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.reactivestreams.Publisher;

import java.util.List;

@Controller("/fruits") // <1>
class FruitController {

    private final FruitService fruitService;

    FruitController(FruitService fruitService) {  // <2>
        this.fruitService = fruitService;
    }

    @Get  // <3>
    Publisher<Fruit> list() {
        return fruitService.list();
    }

    @Post // <4>
    @Status(HttpStatus.CREATED) // <5>
    @SingleResult
    Publisher<Fruit> save(@NonNull @NotNull @Valid Fruit fruit) { // <6>
        return fruitService.save(fruit);
    }

    @Put
    @SingleResult
    Publisher<Fruit> update(@NonNull @NotNull @Valid Fruit fruit) {
        return fruitService.save(fruit);
    }

    @Get("/{id}") // <7>
    Publisher<Fruit> find(@PathVariable String id) {
        return fruitService.find(id);
    }

    @Get("/q") // <8>
    Publisher<Fruit> query(@QueryValue @NotNull List<String> names) { // <9>
        return fruitService.findByNameInList(names);
    }
}
