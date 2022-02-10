package example.micronaut

import io.micronaut.core.annotation.NonNull
import io.micronaut.context.annotation.DefaultImplementation

@DefaultImplementation(BookRepositoryImpl)
interface BookRepository {

    @NonNull
    List<Book> findAll()
}
