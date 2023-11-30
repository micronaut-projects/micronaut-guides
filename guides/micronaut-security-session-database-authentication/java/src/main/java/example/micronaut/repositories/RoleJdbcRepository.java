package example.micronaut.repositories;

import example.micronaut.entities.Role;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;
import java.util.Optional;

@JdbcRepository(dialect = Dialect.POSTGRES) // <1>
public interface RoleJdbcRepository extends CrudRepository<Role, Long> { // <2>

    Role save(String authority);

    Optional<Role> findByAuthority(String authority);
}
