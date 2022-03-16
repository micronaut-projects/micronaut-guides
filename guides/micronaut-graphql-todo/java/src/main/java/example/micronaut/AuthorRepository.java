package example.micronaut;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;

import static io.micronaut.data.model.query.builder.sql.Dialect.POSTGRES;

@JdbcRepository(dialect = POSTGRES) // <1>
public interface AuthorRepository extends CrudRepository<Author, Long> { // <2>

    Optional<Author> findByUsername(String username); // <3>

    Collection<Author> findByIdIn(Collection<Long> ids); // <4>

    default Author findOrCreate(String username) { // <5>
        return findByUsername(username).orElseGet(() -> save(new Author(username)));
    }
}
