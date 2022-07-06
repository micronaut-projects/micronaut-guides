package example.micronaut;

import jakarta.inject.Singleton;
import org.hibernate.SessionFactory;
import org.hibernate.reactive.stage.Stage;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Singleton
public class GenreRepositoryImpl implements GenreRepository {

    private final Stage.SessionFactory sessionFactory;

    public GenreRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory.unwrap(Stage.SessionFactory.class);
    }

    @Override
    public Publisher<Genre> findAll() {
        return Mono.fromCompletionStage(sessionFactory.withTransaction(session -> session.createQuery("from Genre ", Genre.class).getResultList())).flatMapMany(Flux::fromIterable);
    }

    @Override
    public Mono<Genre> create(String name) {
        return Mono.fromCompletionStage(sessionFactory.withTransaction(session -> {
            Genre entity = new Genre(name);
            return session.persist(entity).thenApply(v -> entity);
        }));
    }
}
