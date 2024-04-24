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

import example.micronaut.domain.Genre
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.Repository
import io.micronaut.data.exceptions.DataAccessException
import io.micronaut.data.repository.reactive.ReactorPageableRepository
import reactor.core.publisher.Mono
import jakarta.transaction.Transactional
import jakarta.validation.constraints.NotBlank

@Repository // <1>
abstract class GenreRepository: ReactorPageableRepository<Genre, Long> { // <2>

    open fun save(@NotBlank name: String): Mono<Genre?> {
        return save(Genre(id = null, name = name))
    }

    @Transactional
    open fun saveWithException(@NotBlank name: String): Mono<Genre> {
        return save(name)
            .handle { _, sink ->
                sink.error(DataAccessException("test exception"))
            }
    }

    open fun update(@Id id: Long, @NotBlank name: String): Mono<Genre> {
        return update(Genre(id = id, name = name))
    }
}