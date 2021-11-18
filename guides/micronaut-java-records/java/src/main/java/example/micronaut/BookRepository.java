package example.micronaut;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;
import java.util.List;

@JdbcRepository(dialect = Dialect.POSTGRES) // <1>
public interface BookRepository extends CrudRepository<Book, String> { // <2>
    List<BookCard> find(); // <3>
}
