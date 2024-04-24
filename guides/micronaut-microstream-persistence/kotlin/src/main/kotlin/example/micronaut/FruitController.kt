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
package example.micronaut

import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.annotation.Status
import io.micronaut.scheduling.TaskExecutors.BLOCKING
import io.micronaut.scheduling.annotation.ExecuteOn
import jakarta.inject.Inject
import jakarta.validation.Valid

@Controller("/fruits") // <1>
open class FruitController {

    @Inject
    private lateinit var fruitRepository: FruitRepository // <2>

    @Get // <3>
    fun list() = fruitRepository.list()

    @ExecuteOn(BLOCKING)
    @Post // <4>
    @Status(HttpStatus.CREATED) // <5>
    open fun create(@Valid @Body fruit: FruitCommand) // <6>
        = fruitRepository.create(fruit)

    @ExecuteOn(BLOCKING)
    @Put
    open fun update(@Valid @Body fruit: FruitCommand) = fruitRepository.update(fruit)

    @Get("/{name}") // <7>
    fun find(@PathVariable name: String): Fruit? = fruitRepository.find(name)

    @ExecuteOn(BLOCKING)
    @Delete
    @Status(HttpStatus.NO_CONTENT)
    open fun delete(@Valid @Body fruit: FruitCommand) = fruitRepository.delete(fruit)
}