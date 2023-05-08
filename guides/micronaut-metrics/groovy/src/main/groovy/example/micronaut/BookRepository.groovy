package example.micronaut

import io.micronaut.core.annotation.NonNull
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.repository.CrudRepository

import jakarta.validation.constraints.NotBlank

import static io.micronaut.data.model.query.builder.sql.Dialect.H2

@JdbcRepository(dialect = H2) // <1>
interface BookRepository extends CrudRepository<Book, Long> { // <2>

    @NonNull
    Optional<Book> findByIsbn(@NotBlank String isbn)
}
