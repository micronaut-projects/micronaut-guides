package example.micronaut;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@JdbcRepository(dialect = Dialect.H2) // <1>
public interface BookRepository extends CrudRepository<Book, Long> {  // <2>
    @Query(value = "SELECT * FROM book WHERE framework = :tenant") // <3>
    List<Book> findAllByTenant(String tenant);
}
