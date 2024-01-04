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

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

@Controller("/books") // <1>
public class BookController {

    private final CatalogueClient catalogueClient; // <2>
    private final InventoryClient inventoryClient; // <2>

    public BookController(CatalogueClient catalogueClient, InventoryClient inventoryClient) { // <2>
        this.catalogueClient = catalogueClient;
        this.inventoryClient = inventoryClient;
    }

    @Get // <3>
    public Publisher<BookRecommendation> index() {
        return Flux.from(catalogueClient.findAll(null))
                .flatMap(Flux::fromIterable)
                .flatMap(book -> Flux.from(inventoryClient.stock(book.getIsbn()))
                        .filter(Boolean::booleanValue)
                        .map(response -> book))
                .map(book -> new BookRecommendation(book.getName()));
    }
}
