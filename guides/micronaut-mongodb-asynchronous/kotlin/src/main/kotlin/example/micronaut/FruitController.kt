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
package example.micronaut

import io.micronaut.http.HttpStatus
import io.micronaut.http.HttpStatus.CONFLICT
import io.micronaut.http.HttpStatus.CREATED
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono
import jakarta.validation.Valid

@Controller("/fruits") // <1>
open class FruitController(private val fruitService: FruitRepository) { // <2>

    @Get // <3>
    fun list(): Publisher<Fruit> = fruitService.list()

    @Post // <4>
    open fun save(@Valid fruit: Fruit): Mono<HttpStatus> { // <5>
        return fruitService.save(fruit) // <6>
                .map { added: Boolean -> if (added) CREATED else CONFLICT }
    }
}
