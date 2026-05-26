/*
 * Copyright 2017-2026 original authors
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
package example.micronaut

import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.annotation.Status
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import java.util.Optional

@Controller("/fruits") // <1>
@ExecuteOn(TaskExecutors.BLOCKING) // <2>
class FruitController(private val fruitService: FruitService) { // <3>

    @Get // <4>
    fun list(): Iterable<Fruit> {
        return fruitService.list()
    }

    @Post // <5>
    @Status(HttpStatus.CREATED) // <6>
    fun save(@Body @Valid fruit: Fruit): Fruit { // <7>
        return fruitService.save(fruit)
    }

    @Put
    fun update(@Body @Valid fruit: Fruit): Fruit {
        return fruitService.save(fruit)
    }

    @Get("/{id}") // <8>
    fun find(@PathVariable id: String): Optional<Fruit> {
        return fruitService.find(id)
    }

    @Get("/q") // <9>
    fun query(@QueryValue @NotNull names: List<String>): Iterable<Fruit> { // <10>
        return fruitService.findByNameInList(names)
    }
}
