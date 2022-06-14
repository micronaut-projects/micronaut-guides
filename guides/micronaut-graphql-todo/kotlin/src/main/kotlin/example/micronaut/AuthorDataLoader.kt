package example.micronaut

import io.micronaut.scheduling.TaskExecutors
import jakarta.inject.Named
import jakarta.inject.Singleton
import org.dataloader.MappedBatchLoader
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import java.util.concurrent.ExecutorService

@Singleton // <1>
class AuthorDataLoader(
    private val authorRepository: AuthorRepository,
    @Named(TaskExecutors.IO) val executor: ExecutorService // <2>
) : MappedBatchLoader<Long, Author> {

    override fun load(keys: MutableSet<Long>): CompletionStage<Map<Long, Author>> =
        CompletableFuture.supplyAsync({
            authorRepository
                .findByIdIn(keys.toList())
                .associateBy { it.id!! }
        }, executor)
}