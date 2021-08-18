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
