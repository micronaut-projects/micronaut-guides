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

import io.micronaut.core.annotation.NonNull;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface BookRepository {
    boolean existsTable();

    void createTable();

    @NonNull
    List<Book> findAll();

    void save(@NonNull @NotNull @Valid Book book);

    @NonNull
    Optional<Book> findById(@NonNull @NotBlank String id);

    void delete(@NonNull @NotBlank String id);

    @NonNull
    Optional<Book> findByIsbn(@NonNull @NotBlank String isbn);
}
