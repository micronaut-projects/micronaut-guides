package example.micronaut

import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import org.jspecify.annotations.Nullable

@JdbcRepository(dialect = Dialect.ORACLE) // <1>
interface BookRepository extends CrudRepository<Book, Long> { // <2>

    @Nullable
    Book findByTitle(String title)

    void upsert(Book book) // <3>
}
