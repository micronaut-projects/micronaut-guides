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
import io.micronaut.transaction.annotation.ReadOnly
import io.micronaut.transaction.annotation.Transactional
import jakarta.inject.Singleton

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceException
import jakarta.persistence.TypedQuery
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

@Singleton // <1>
class GenreRepositoryImpl implements GenreRepository {

    private static final List<String> VALID_PROPERTY_NAMES = ['id', 'name']

    private final EntityManager entityManager  // <2>
    private final ApplicationConfiguration applicationConfiguration

    GenreRepositoryImpl(EntityManager entityManager,
                        ApplicationConfiguration applicationConfiguration) { // <2>
        this.entityManager = entityManager
        this.applicationConfiguration = applicationConfiguration
    }

    @Override
    @ReadOnly  // <3>
    Optional<Genre> findById(long id) {
        Optional.ofNullable(entityManager.find(Genre, id))
    }

    @Override
    @Transactional // <4>
    Genre save(@NotBlank String name) {
        Genre genre = new Genre(name)
        entityManager.persist(genre)
        genre
    }

    @Override
    @Transactional // <4>
    void deleteById(long id) {
        findById(id).ifPresent(entityManager::remove)
    }

    @ReadOnly // <3>
    List<Genre> findAll(@NotNull SortingAndOrderArguments args) {
        String qlString = 'SELECT g FROM Genre as g'
        if (args.order && args.sort && VALID_PROPERTY_NAMES.contains(args.sort)) {
            qlString += ' ORDER BY g.' + args.sort + ' ' + args.order.toLowerCase()
        }
        TypedQuery<Genre> query = entityManager.createQuery(qlString, Genre)
        query.maxResults = args.max != null ? args.max : applicationConfiguration.max
        if (args.offset) {
            query.firstResult = args.offset
        }
        query.resultList
    }

    @Override
    @Transactional // <4>
    int update(long id, @NotBlank String name) {
        entityManager.createQuery('UPDATE Genre g SET name = :name where id = :id')
                .setParameter('name', name)
                .setParameter('id', id)
                .executeUpdate()
    }

    @Override // <4>
    @Transactional // <4>
    Genre saveWithException(@NotBlank String name) {
        save(name)
        throw new PersistenceException()
    }
}
