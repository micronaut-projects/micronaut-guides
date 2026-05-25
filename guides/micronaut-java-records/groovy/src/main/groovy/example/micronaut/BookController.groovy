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

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.NonNull
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn

import java.math.RoundingMode

@CompileStatic
@Controller('/books') // <1>
class BookController {

    private final BookRepository bookRepository
    private final ValueAddedTaxConfiguration valueAddedTaxConfiguration

    BookController(BookRepository bookRepository, // <2>
                   ValueAddedTaxConfiguration valueAddedTaxConfiguration) {
        this.bookRepository = bookRepository
        this.valueAddedTaxConfiguration = valueAddedTaxConfiguration
    }

    @ExecuteOn(TaskExecutors.BLOCKING) // <3>
    @Get // <4>
    List<BookForSale> index() { // <5>
        bookRepository.find()
                .collect { bookCard ->
                    new BookForSale(bookCard.isbn(),
                            bookCard.title(),
                            salePrice(bookCard))
                }
    }

    @NonNull
    private BigDecimal salePrice(@NonNull BookCard bookCard) {
        bookCard.price()
                .add(bookCard.price()
                        .multiply(valueAddedTaxConfiguration.percentage()
                                .divide(new BigDecimal('100.0'), 2, RoundingMode.HALF_DOWN)))
                .setScale(2, RoundingMode.HALF_DOWN)
    }
}
