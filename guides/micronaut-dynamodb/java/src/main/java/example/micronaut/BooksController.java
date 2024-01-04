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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

@ExecuteOn(TaskExecutors.BLOCKING) // <1>
@Controller("/books") // <2>
public class BooksController {

    private final BookRepository bookRepository;

    public BooksController(BookRepository bookRepository) { // <3>
        this.bookRepository = bookRepository;
    }

    @Get // <4>
    public List<Book> index() {
        return bookRepository.findAll();
    }

    @Post // <5>
    public HttpResponse<?> save(@Body("isbn") @NonNull @NotBlank String isbn, // <6>
                             @Body("name") @NonNull @NotBlank String name) {
        String id = bookRepository.save(isbn, name);
        return HttpResponse.created(UriBuilder.of("/books").path(id).build());
    }

    @Get("/{id}") // <7>
    public Optional<Book> show(@PathVariable @NonNull @NotBlank String id) { // <8>
        return bookRepository.findById(id);
    }

    @Delete("/{id}") // <9>
    @Status(HttpStatus.NO_CONTENT) // <10>
    public void delete(@PathVariable @NonNull @NotBlank String id) {
        bookRepository.delete(id);
    }
}
