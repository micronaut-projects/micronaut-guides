package example.micronaut;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.context.annotation.DefaultImplementation;

import java.util.List;

@DefaultImplementation(BookRepositoryImpl.class)
public interface BookRepository {

    @NonNull
    List<Book> findAll();
}
