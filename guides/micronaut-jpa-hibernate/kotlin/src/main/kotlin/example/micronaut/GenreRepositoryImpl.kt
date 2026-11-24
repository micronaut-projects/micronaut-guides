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
import io.micronaut.transaction.annotation.ReadOnly
import io.micronaut.transaction.annotation.Transactional
import jakarta.inject.Singleton
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceException
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

@Singleton // <1>
open class GenreRepositoryImpl(
    private val entityManager: EntityManager, // <2>
    private val applicationConfiguration: ApplicationConfiguration
) : GenreRepository {

    private val validPropertyNames = listOf("id", "name")

    @ReadOnly // <3>
    override fun findById(id: Long): Genre? {
        return entityManager.find(Genre::class.java, id)
    }

    @Transactional // <4>
    override fun save(@NotBlank name: String): Genre {
        val genre = Genre(name = name)
        entityManager.persist(genre)
        return genre
    }

    @Transactional // <4>
    override fun deleteById(id: Long) {
        findById(id)?.let { entityManager.remove(it) }
    }

    @ReadOnly // <3>
    override fun findAll(@NotNull args: SortingAndOrderArguments): List<Genre> {
        var qlString = "SELECT g FROM Genre as g"
        val order = args.order
        val sort = args.sort
        if (order != null && sort != null && validPropertyNames.contains(sort)) {
            qlString += " ORDER BY g.$sort ${order.lowercase()}"
        }
        val query = entityManager.createQuery(qlString, Genre::class.java)
        query.maxResults = args.max ?: applicationConfiguration.max
        args.offset?.let { query.firstResult = it }
        return query.resultList
    }

    @Transactional // <4>
    override fun update(id: Long, @NotBlank name: String): Int {
        return entityManager.createQuery("UPDATE Genre g SET name = :name where id = :id")
            .setParameter("name", name)
            .setParameter("id", id)
            .executeUpdate()
    }

    @Transactional // <4>
    override fun saveWithException(@NotBlank name: String): Genre {
        save(name)
        throw PersistenceException()
    }
}
