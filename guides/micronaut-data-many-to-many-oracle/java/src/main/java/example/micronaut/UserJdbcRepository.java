/*
 * Copyright 2017-2025 original authors
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
package example.micronaut;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@JdbcRepository(dialect = Dialect.ORACLE) // <1>
public interface UserJdbcRepository extends CrudRepository<UserEntity, Long> { // <2>
    UserEntity save(String username);

    // <3>
    @Query(value = """
            SELECT
                u.id,
                u.username,
                LISTAGG(r.authority, ',') WITHIN GROUP (ORDER BY r.authority) AS authorities
            FROM users u
            LEFT JOIN user_role ur ON ur.id_user_id = u.id
            LEFT JOIN role r ON r.id = ur.id_role_id
            WHERE u.username = :username
            GROUP BY u.id, u.username
            """)
    Optional<User> findByUsername(String username);
}
