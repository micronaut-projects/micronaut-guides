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
package example.micronaut;

import example.micronaut.model.BookAvailability;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@JdbcRepository(dialect = Dialect.POSTGRES)                                 // <1>
public interface BookRepository extends CrudRepository<BookEntity, Long> {  // <2>
    @NonNull
    List<BookEntity> findAll();


    @NonNull
    BookEntity save(@NonNull @NotBlank String name,                         // <3>
                    @NonNull @NotNull BookAvailability availability,
                    @NonNull @NotBlank String author,
                    @NonNull @NotBlank String isbn);

    @NonNull
    List<BookEntity> findAllByAuthorContains(String author);                // <4>

    @NonNull
    List<BookEntity> findAllByNameContains(String name);                    // <5>

    @NonNull
    List<BookEntity> findAllByAuthorContainsAndNameContains(                // <6>
            String author,
            String name
    );
}
