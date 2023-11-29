package example.micronaut.repositories;

import example.micronaut.entities.User;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;
import jakarta.validation.constraints.NotBlank;

import java.util.Optional;

@JdbcRepository(dialect = Dialect.POSTGRES) // <1>
public interface UserJdbcRepository extends CrudRepository<User, Long> { // <2>
    Optional<User> findByUsername(@NonNull @NotBlank String username);
}
