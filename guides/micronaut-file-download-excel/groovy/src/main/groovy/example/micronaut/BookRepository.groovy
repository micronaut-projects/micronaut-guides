package example.micronaut

import edu.umd.cs.findbugs.annotations.NonNull
import io.micronaut.context.annotation.DefaultImplementation

@DefaultImplementation(BookRepositoryImpl.class)
interface BookRepository {

    @NonNull
    List<Book> findAll()
}
