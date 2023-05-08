package example.micronaut

import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect.H2
import io.micronaut.data.repository.CrudRepository
import java.util.Optional
import jakarta.validation.constraints.NotBlank

@JdbcRepository(dialect = H2) // <1>
interface BookRepository : CrudRepository<Book, Long> { // <2>

    fun findByIsbn(isbn: @NotBlank String): Optional<Book>
}
