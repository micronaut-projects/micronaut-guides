package example.micronaut;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@JdbcRepository(dialect = Dialect.POSTGRES) // <1>
public interface AuthorRepository extends CrudRepository<Author, Long> {

    Optional<Author> findByUsername(String username); // <2>

    Collection<Author> findByIdInList(Collection<Long> ids); // <3>

    @Transactional
    default Author findOrCreate(String username) { // <4>
        return findByUsername(username).orElseGet(() -> save(new Author(username)));
    }
}
