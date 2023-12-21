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
package example.micronaut.domain

import groovy.transform.CompileStatic
import io.micronaut.serde.annotation.Serdeable

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull

import static jakarta.persistence.GenerationType.AUTO

@CompileStatic
@Serdeable
@Entity
@Table(name = 'book')
class Book {

    @Id
    @GeneratedValue(strategy = AUTO)
    Long id

    @NotNull
    @Column(name = 'name', nullable = false)
    String name

    @NotNull
    @Column(name = 'isbn', nullable = false)
    String isbn

    @ManyToOne
    Genre genre

    Book() {}

    Book(@NotNull String isbn,
         @NotNull String name,
         Genre genre) {
        this.isbn = isbn
        this.name = name
        this.genre = genre
    }

    @Override
    String toString() {
        "Book{id=$id, name='$name', isbn='$isbn', genre=$genre}"
    }
}
