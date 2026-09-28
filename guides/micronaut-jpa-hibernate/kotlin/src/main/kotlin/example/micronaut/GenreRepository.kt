/*
 * Copyright 2017-2026 original authors
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

import example.micronaut.domain.Genre
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

interface GenreRepository {

    fun findById(id: Long): Genre?

    fun save(@NotBlank name: String): Genre

    fun saveWithException(@NotBlank name: String): Genre

    fun deleteById(id: Long)

    fun findAll(@NotNull args: SortingAndOrderArguments): List<Genre>

    fun update(id: Long, @NotBlank name: String): Int
}
