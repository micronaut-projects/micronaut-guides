package example.micronaut

import example.micronaut.domain.User
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository

@JdbcRepository(dialect = Dialect.H2) // <1>
interface UserJdbcRepository extends CrudRepository<User, Long> { // <2>
    Optional<User> findByUsername(String username)
}
