package example.micronaut.repositories;

import example.micronaut.entities.UserRole;
import example.micronaut.entities.UserRoleId;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@JdbcRepository(dialect = Dialect.POSTGRES) // <1>
public interface UserRoleJdbcRepository extends CrudRepository<UserRole, UserRoleId> { // <2>

    @Query("""
    SELECT authority FROM role 
    INNER JOIN user_role ON user_role.id_role_id = role.id 
    INNER JOIN "user" user_ ON user_role.id_user_id = user_.id 
    WHERE user_.username = :username""") // <3>
    List<String> findAllAuthoritiesByUsername(@NotBlank String username);
}
