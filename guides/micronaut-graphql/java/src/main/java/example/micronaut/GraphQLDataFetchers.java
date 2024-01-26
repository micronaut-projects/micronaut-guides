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

import graphql.schema.DataFetcher;

import jakarta.inject.Singleton;

@Singleton
public class GraphQLDataFetchers {

    private final DbRepository dbRepository;

    public GraphQLDataFetchers(DbRepository dbRepository) { // <1>
        this.dbRepository = dbRepository;
    }

    public DataFetcher<Book> getBookByIdDataFetcher() {
        return dataFetchingEnvironment -> { // <2>
            String bookId = dataFetchingEnvironment.getArgument("id"); // <3>
            return dbRepository.findAllBooks() // <4>
                    .stream()
                    .filter(book -> book.getId().equals(bookId))
                    .findFirst()
                    .orElse(null);
        };
    }

    public DataFetcher<Author> getAuthorDataFetcher() {
        return dataFetchingEnvironment -> {
            Book book = dataFetchingEnvironment.getSource(); // <5>
            Author authorBook = book.getAuthor(); // <6>
            return dbRepository.findAllAuthors() // <7>
                    .stream()
                    .filter(author -> author.getId().equals(authorBook.getId()))
                    .findFirst()
                    .orElse(null);
        };
    }
}
