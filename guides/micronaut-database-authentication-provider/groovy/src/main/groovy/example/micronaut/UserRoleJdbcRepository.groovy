package example.micronaut

import example.micronaut.domain.UserRole
import example.micronaut.domain.UserRoleId
import io.micronaut.data.annotation.Query
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository

@JdbcRepository(dialect = Dialect.H2) // <1>
interface UserRoleJdbcRepository extends CrudRepository<UserRole, UserRoleId> {  // <2>
    @Query("""SELECT role_.`authority` FROM `role` role_ 
    inner join `user_role` user_role_ ON user_role_.`user_role_id_role_id` = role_.`id` 
    inner join `user` user_ ON user_role_.`user_role_id_user_id` = user_.`id` 
    where user_.`username` = :username""") // <2>
    List<String> findAllAuthoritiesByUsername(String username)
}
