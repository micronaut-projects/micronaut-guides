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

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import java.util.Optional

@Client("/fruits")
interface FruitClient {

    @Get
    fun list(): Iterable<Fruit>

    @Get("/{id}")
    fun find(@PathVariable id: String): Optional<Fruit>

    @Get("/q")
    fun query(@QueryValue @NotNull names: List<String>): Iterable<Fruit>

    @Post
    fun save(@Body @Valid fruit: Fruit): HttpResponse<Fruit>

    @Put
    fun update(@Body @Valid fruit: Fruit): Fruit
}
