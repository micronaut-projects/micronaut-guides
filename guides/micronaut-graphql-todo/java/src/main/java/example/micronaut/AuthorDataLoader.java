package example.micronaut;

import io.micronaut.scheduling.TaskExecutors;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.dataloader.MappedBatchLoader;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@Singleton // <1>
public class AuthorDataLoader implements MappedBatchLoader<Long, Author> {

    private final AuthorRepository authorRepository;
    private final ExecutorService executor;

    public AuthorDataLoader(
            AuthorRepository authorRepository,
            @Named(TaskExecutors.IO) ExecutorService executor // <2>
    ) {
        this.authorRepository = authorRepository;
        this.executor = executor;
    }

    @Override
    public CompletionStage<Map<Long, Author>> load(Set<Long> keys) {
        return CompletableFuture.supplyAsync(() -> authorRepository
                        .findByIdIn(keys)
                        .stream()
                        .collect(toMap(Author::getId, Function.identity())),
                executor
        );
    }

}
