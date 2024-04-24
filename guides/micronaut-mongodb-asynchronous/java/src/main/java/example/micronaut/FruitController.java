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
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import static io.micronaut.http.HttpStatus.CONFLICT;
import static io.micronaut.http.HttpStatus.CREATED;

@Controller("/fruits") // <1>
class FruitController {

    private final FruitRepository fruitService;

    FruitController(FruitRepository fruitService) {  // <2>
        this.fruitService = fruitService;
    }

    @Get // <3>
    Publisher<Fruit> list() {
        return fruitService.list();
    }

    @Post // <4>
    Mono<HttpStatus> save(@NonNull @NotNull @Valid Fruit fruit) { // <5>
        return fruitService.save(fruit) // <6>
                .map(added -> added ? CREATED : CONFLICT);
    }
}
