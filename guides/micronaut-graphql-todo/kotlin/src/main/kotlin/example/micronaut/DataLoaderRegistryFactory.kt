package example.micronaut

import io.micronaut.context.annotation.Factory
import io.micronaut.runtime.http.scope.RequestScope
import org.dataloader.DataLoader
import org.dataloader.DataLoaderRegistry
import org.slf4j.LoggerFactory

@Factory // <1>
class DataLoaderRegistryFactory {

    companion object {
        private val LOG = LoggerFactory.getLogger(DataLoaderRegistryFactory::class.java)
    }

    @Suppress("unused")
    @RequestScope // <2>
    fun dataLoaderRegistry(authorDataLoader: AuthorDataLoader): DataLoaderRegistry {
        val dataLoaderRegistry = DataLoaderRegistry()
        dataLoaderRegistry.register(
            "author",
            DataLoader.newMappedDataLoader(authorDataLoader)
        ) // <3>

        LOG.trace("Created new data loader registry")

        return dataLoaderRegistry
    }

}