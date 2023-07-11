package example.micronaut

import example.micronaut.domain.Role
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository

@JdbcRepository(dialect = Dialect.H2) // <1>
interface RoleJdbcRepository extends CrudRepository<Role, Long> { // <2>
    Role save(String authority)
    Optional<Role> findByAuthority(String authority)
}
