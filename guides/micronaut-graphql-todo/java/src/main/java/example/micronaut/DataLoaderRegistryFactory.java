package example.micronaut;

import io.micronaut.context.annotation.Factory;
import io.micronaut.runtime.http.scope.RequestScope;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Factory
public class DataLoaderRegistryFactory {

    private static final Logger LOG = LoggerFactory.getLogger(DataLoaderRegistryFactory.class);

    private final AuthorDataLoader authorDataLoader;

    public DataLoaderRegistryFactory(AuthorDataLoader authorDataLoader) {
        this.authorDataLoader = authorDataLoader;
    }

    @SuppressWarnings("unused")
    @RequestScope // <1>
    public DataLoaderRegistry dataLoaderRegistry() {
        DataLoaderRegistry dataLoaderRegistry = new DataLoaderRegistry();
        dataLoaderRegistry.register(
                "author",
                DataLoader.newMappedDataLoader(authorDataLoader)
        ); // <2>

        LOG.trace("Created new data loader registry");

        return dataLoaderRegistry;
    }

}