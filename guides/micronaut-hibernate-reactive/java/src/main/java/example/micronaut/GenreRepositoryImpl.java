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
                    Stage.Query<Genre> query = session.createQuery(qlString, Genre.class);
                    query.setMaxResults(args.getMax().orElseGet(applicationConfiguration::getMax));
                    args.getOffset().ifPresent(query::setFirstResult);
                    return query.getResultList();
                }))
            .flatMapMany(Flux::fromIterable);
    }

    private String createQuery(SortingAndOrderArguments args) {
        String qlString = "SELECT g FROM Genre as g";
        if (args.getOrder().isPresent() && args.getSort().isPresent() && VALID_PROPERTY_NAMES.contains(args.getSort().get())) {
            qlString += " ORDER BY g." + args.getSort().get() + ' ' + args.getOrder().get().toLowerCase();
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
