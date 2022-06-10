package example.micronaut

import io.micronaut.core.annotation.NonNull
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.repository.CrudRepository

import javax.validation.constraints.NotBlank

import static io.micronaut.data.model.query.builder.sql.Dialect.MYSQL

@JdbcRepository(dialect = MYSQL) // <1>
interface BookRepository extends CrudRepository<Book, Long> { // <2>

    @NonNull
    List<Book> findAll()

    @NonNull
    Optional<Book> findByIsbn(@NotBlank String isbn)
}
