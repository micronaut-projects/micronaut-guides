package example.micronaut

import graphql.schema.DataFetcher

import jakarta.inject.Singleton

@Singleton
class GraphQLDataFetchers {

    private final DbRepository dbRepository

    GraphQLDataFetchers(DbRepository dbRepository) { // <1>
        this.dbRepository = dbRepository
    }

    DataFetcher<Book> getBookByIdDataFetcher() {
        return { dataFetchingEnvironment ->  // <2>
                {
                    String bookId = dataFetchingEnvironment.getArgument("id") // <3>
                    return dbRepository.findAllBooks() // <4>
                        .stream()
                        .filter(book -> book.id == bookId)
                        .findFirst()
                        .orElse(null)
                }
        }
    }

    DataFetcher<Author> getAuthorDataFetcher() {
        return { dataFetchingEnvironment ->
                {
                    Book book = dataFetchingEnvironment.getSource() // <5>
                    Author authorBook = book.getAuthor() // <6>
                    return dbRepository.findAllAuthors() // <7>
                        .stream()
                        .filter(author -> author.id == authorBook.id)
                        .findFirst()
                        .orElse(null)
                }
        }
    }
}