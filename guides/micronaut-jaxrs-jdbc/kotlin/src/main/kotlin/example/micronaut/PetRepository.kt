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
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import java.util.*
import jakarta.validation.constraints.NotBlank

@JdbcRepository(dialect = Dialect.MYSQL) // <1>
interface PetRepository : CrudRepository<Pet, Long> { // <2>
    fun list() : List<NameDto> // <3>

    fun findByName(@NotBlank name: String) : Optional<Pet>

    fun save(@NotBlank name: String, type: PetType) : Pet
}