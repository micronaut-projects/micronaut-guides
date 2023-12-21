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

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux

@Controller("/books") // <1>
class BookController(
        private val catalogueClient: CatalogueClient, // <2>
        private val inventoryClient: InventoryClient) {

    @Get // <3>
    fun index(): Publisher<BookRecommendation> =
        Flux.from(catalogueClient.findAll(null))
                .flatMap { Flux.fromIterable(it) }
                .flatMap { book: Book ->
                    Flux.from(inventoryClient.stock(book.isbn))
                            .filter { obj: Boolean? -> obj == true }
                            .map { book }
                }
                .map { (_, name): Book -> BookRecommendation(name) }
}
