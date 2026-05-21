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
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Status
import io.micronaut.http.uri.UriBuilder
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import jakarta.validation.constraints.NotBlank
import java.util.Optional

@ExecuteOn(TaskExecutors.BLOCKING) // <1>
@Controller("/books") // <2>
open class BooksController(private val bookRepository: BookRepository) { // <3>

    @Get // <4>
    open fun index(): List<Book> = bookRepository.findAll()

    @Post // <5>
    open fun save(
        @Body("isbn") @NotBlank isbn: String, // <6>
        @Body("name") @NotBlank name: String
    ): HttpResponse<*> {
        val id = bookRepository.save(isbn, name)
        return HttpResponse.created<Any>(UriBuilder.of("/books").path(id).build())
    }

    @Get("/{id}") // <7>
    open fun show(@PathVariable @NotBlank id: String): Optional<Book> = // <8>
        bookRepository.findById(id)

    @Delete("/{id}") // <9>
    @Status(HttpStatus.NO_CONTENT) // <10>
    open fun delete(@PathVariable @NotBlank id: String) {
        bookRepository.delete(id)
    }
}
