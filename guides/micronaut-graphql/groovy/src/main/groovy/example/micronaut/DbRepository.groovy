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
package example.micronaut

import jakarta.inject.Singleton

@Singleton
class DbRepository {

    private static final List<Book> books = [ // <1>
        new Book("book-1", "Harry Potter and the Philosopher's Stone", 223, new Author("author-1", "Joanne", "Rowling")),
        new Book("book-2", "Moby Dick", 635, new Author("author-2", "Herman", "Melville")),
        new Book("book-3", "Interview with the vampire", 371, new Author("author-3", "Anne", "Rice"))
    ]

    List<Book> findAllBooks() {
        return books
    }

    List<Author> findAllAuthors() {
        return books
            .collect { it.author }
    }
}
