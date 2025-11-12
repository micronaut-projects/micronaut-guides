/*
 * Copyright 2017-2024 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
    where user_.`username` = :username""") // <3>
    List<String> findAllAuthoritiesByUsername(String username)
}
