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

import io.micronaut.core.async.annotation.SingleResult
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.annotation.Status
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.reactivestreams.Publisher

@Controller("/fruits") // <1>
class FruitController(private val fruitService: FruitService) { // <2>

    @Get // <3>
    fun list(): Publisher<Fruit> {
        return fruitService.list()
    }

    @Post // <4>
    @Status(HttpStatus.CREATED) // <5>
    @SingleResult
    fun save(@Body @Valid fruit: Fruit): Publisher<Fruit> { // <6>
        return fruitService.save(fruit)
    }

    @Put
    @SingleResult
    fun update(@Body @Valid fruit: Fruit): Publisher<Fruit> {
        return fruitService.save(fruit)
    }

    @Get("/{id}") // <7>
    fun find(@PathVariable id: String): Publisher<Fruit> {
        return fruitService.find(id)
    }

    @Get("/q") // <8>
    fun query(@QueryValue @NotNull names: List<String>): Publisher<Fruit> { // <9>
        return fruitService.findByNameInList(names)
    }
}
