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
package example.micronaut

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import java.util.function.Predicate
import jakarta.inject.Singleton

@Singleton
class GraphQLDataFetchers(private val dbRepository: DbRepository) { // <1>

    fun bookByIdDataFetcher(): DataFetcher<Book> {
        return DataFetcher { dataFetchingEnvironment: DataFetchingEnvironment -> // <2>
            val bookId: String = dataFetchingEnvironment.getArgument("id") // <3>
            dbRepository.findAllBooks() // <4>
                .firstOrNull { book: Book -> (book.id == bookId) }
        }
    }

    fun authorDataFetcher(): DataFetcher<Author> {
        return DataFetcher { dataFetchingEnvironment: DataFetchingEnvironment ->
            val book: Book = dataFetchingEnvironment.getSource() // <5>
            val authorBook: Author = book.author // <6>
            dbRepository.findAllAuthors() // <7>
                .firstOrNull {author: Author -> (author.id == authorBook.id) }
        }
    }

}
