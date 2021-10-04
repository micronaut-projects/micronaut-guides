package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.context.annotation.DefaultImplementation;

import java.util.List;

@DefaultImplementation(BookRepositoryImpl.class)
public interface BookRepository {

    @NonNull
    List<Book> findAll();
}
