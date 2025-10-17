/*
 * Copyright 2017-2025 original authors
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

import io.micronaut.http.MediaType.TEXT_PLAIN
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import java.util.Optional
import jakarta.validation.constraints.NotBlank
import io.micronaut.security.rules.SecurityRule.IS_AUTHENTICATED
import io.micronaut.security.annotation.Secured

@Controller("/books")
open class BooksController {

    @Produces(TEXT_PLAIN)
    @Get("/stock/{isbn}")
    @Secured(IS_AUTHENTICATED) // <1>
    open fun stock(@NotBlank isbn: String): Boolean? =
        bookInventoryByIsbn(isbn).map { (_, stock) -> stock > 0 }.orElse(null)

    private fun bookInventoryByIsbn(isbn: String): Optional<BookInventory> {
        if (isbn == "1491950358") {
            return Optional.of(BookInventory(isbn, 4))
        }
        if (isbn == "1680502395") {
            return Optional.of(BookInventory(isbn, 0))
        }
        return Optional.empty()
    }
}
