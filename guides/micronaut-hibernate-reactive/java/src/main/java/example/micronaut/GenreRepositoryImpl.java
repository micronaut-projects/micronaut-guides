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
package example.micronaut;

import example.micronaut.domain.Genre;
import jakarta.inject.Singleton;
import org.hibernate.SessionFactory;
import org.hibernate.reactive.stage.Stage;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import jakarta.persistence.PersistenceException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

@Singleton // <1>
public class GenreRepositoryImpl implements GenreRepository {

    private static final List<String> VALID_PROPERTY_NAMES = Arrays.asList("id", "name");
    private final ApplicationConfiguration applicationConfiguration;
    private final Stage.SessionFactory sessionFactory;

    public GenreRepositoryImpl(
            ApplicationConfiguration applicationConfiguration, // <2>
            SessionFactory sessionFactory // <3>
    ) {
        this.applicationConfiguration = applicationConfiguration;
        this.sessionFactory = sessionFactory.unwrap(Stage.SessionFactory.class);
    }

    @Override
    public Publisher<Optional<Genre>> findById(long id) {
        return Mono.fromCompletionStage(sessionFactory.withTransaction(session -> // <4>
            find(session, id)
        ));
    }

    CompletionStage<Optional<Genre>> find(Stage.Session session, Long id) {
        return session.find(Genre.class, id).thenApply(Optional::ofNullable);
    }

    @Override
    public Publisher<Genre> save(String name) {
        return Mono.fromCompletionStage(sessionFactory.withTransaction(session -> {
            Genre entity = new Genre(name);
            return session.persist(entity).thenApply(v -> entity);
        }));
    }

    @Override
    public Publisher<Genre> saveWithException(String name) {
        return Mono.fromCompletionStage(sessionFactory.withTransaction(session -> {
            Genre entity = new Genre(name);
            return session.persist(entity).thenApply(v -> {
                throw new PersistenceException();
            });
        }));
    }

    @Override
    public void deleteById(long id) {
        sessionFactory.withTransaction(session -> session.find(Genre.class, id).thenApply(session::remove));
    }

    @Override
    public Publisher<Genre> findAll(SortingAndOrderArguments args) {
        String qlString = createQuery(args);
        return Mono.fromCompletionStage(sessionFactory.withTransaction(session -> {
                    Stage.SelectionQuery<Genre> query = session.createQuery(qlString, Genre.class);
                    query.setMaxResults(args.max() == null ? applicationConfiguration.getMax() : args.max());
                    if (args.offset() != null) {
                        query.setFirstResult(args.offset());
                    }
                    return query.getResultList();
                }))
                .flatMapMany(Flux::fromIterable);
    }

    private String createQuery(SortingAndOrderArguments args) {
        String qlString = "SELECT g FROM Genre as g";
        String order = args.order();
        String sort = args.sort();
        if (order != null && sort != null && VALID_PROPERTY_NAMES.contains(sort)) {
            qlString += " ORDER BY g." + sort + ' ' + order.toLowerCase();
        }
        return qlString;
    }

    @Override
    public Publisher<Integer> update(long id, String name) {
        return Mono.fromCompletionStage(sessionFactory.withTransaction(session -> session.createQuery("UPDATE Genre g SET name = :name where id = :id")
                        .setParameter("name", name)
                        .setParameter("id", id)
                        .executeUpdate()));
    }
}
