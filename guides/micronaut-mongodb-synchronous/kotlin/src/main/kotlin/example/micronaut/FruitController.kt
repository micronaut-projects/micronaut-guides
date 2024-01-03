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

import io.micronaut.http.HttpStatus.CREATED
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Status
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import jakarta.validation.Valid

@Controller("/fruits") // <1>
@ExecuteOn(TaskExecutors.BLOCKING) // <2>
open class FruitController(private val fruitService: FruitRepository) { // <3>

    @Get // <4>
    fun list(): List<Fruit> = fruitService.list()

    @Post // <5>
    @Status(CREATED) // <6>
    open fun save(@Valid fruit: Fruit) = // <7>
        fruitService.save(fruit)
}
