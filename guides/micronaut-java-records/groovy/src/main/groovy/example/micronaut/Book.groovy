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
import io.micronaut.core.annotation.Nullable
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@CompileStatic
@MappedEntity // <1>
class Book {

    @Id
    @NonNull
    @NotBlank
    @Size(max = 255)
    final String isbn // <2>

    @NonNull
    @NotBlank
    @Size(max = 255)
    final String title // <3>

    @NonNull
    @NotNull
    final BigDecimal price // <3>

    @Nullable
    final String about // <4>

    Book(@NonNull String isbn,
         @NonNull String title,
         @NonNull BigDecimal price,
         @Nullable String about) {
        this.isbn = isbn
        this.title = title
        this.price = price
        this.about = about
    }

    String isbn() { isbn }

    String title() { title }

    BigDecimal price() { price }

    String about() { about }
}
