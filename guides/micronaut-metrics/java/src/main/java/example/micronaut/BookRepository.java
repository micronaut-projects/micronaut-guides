package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.repository.CrudRepository;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

import static io.micronaut.data.model.query.builder.sql.Dialect.H2;

@JdbcRepository(dialect = H2) // <1>
public interface BookRepository extends CrudRepository<Book, Long> { // <2>

    @NonNull
    Optional<Book> findByIsbn(@NotBlank String isbn);
}
