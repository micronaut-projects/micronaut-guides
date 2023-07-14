package example.micronaut;

import example.micronaut.model.BookAvailability;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@JdbcRepository(dialect = Dialect.POSTGRES)                                 // <1>
public interface BookRepository extends CrudRepository<BookEntity, Long> {  // <2>
    @NonNull
    List<BookEntity> findAll();


    @NonNull
    BookEntity save(@NonNull @NotBlank String name,                         // <3>
                    @NonNull @NotNull BookAvailability availability,
                    @NonNull @NotBlank String author,
                    @NonNull @NotBlank String isbn);

    @NonNull
    List<BookEntity> findAllByAuthorContains(String author);                // <4>

    @NonNull
    List<BookEntity> findAllByNameContains(String name);                    // <5>

    @NonNull
    List<BookEntity> findAllByAuthorContainsAndNameContains(                // <6>
            String author,
            String name
    );
}
