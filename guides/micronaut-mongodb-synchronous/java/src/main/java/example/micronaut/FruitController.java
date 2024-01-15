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
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Status;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

import static io.micronaut.http.HttpStatus.CREATED;

@Controller("/fruits") // <1>
@ExecuteOn(TaskExecutors.BLOCKING)  // <2>
class FruitController {

    private final FruitRepository fruitService;

    FruitController(FruitRepository fruitService) {  // <3>
        this.fruitService = fruitService;
    }

    @Get  // <4>
    List<Fruit> list() {
        return fruitService.list();
    }

    @Post // <5>
    @Status(CREATED) // <6>
    void save(@NonNull @NotNull @Valid Fruit fruit) { // <7>
        fruitService.save(fruit);
    }
}
