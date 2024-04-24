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

import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect.H2
import io.micronaut.data.repository.CrudRepository
import java.util.Optional
import jakarta.transaction.Transactional
import jakarta.validation.constraints.NotBlank

@JdbcRepository(dialect = H2) // <1>
interface RefreshTokenRepository : CrudRepository<RefreshTokenEntity, Long> { // <2>

    @Transactional
    fun save(@NotBlank username: String,
             @NotBlank refreshToken: String,
             revoked: Boolean): RefreshTokenEntity // <3>

    fun findByRefreshToken(@NotBlank refreshToken: String): Optional<RefreshTokenEntity> // <4>

    fun updateByUsername(@NotBlank username: String,
                         revoked: Boolean): Long // <5>
}
