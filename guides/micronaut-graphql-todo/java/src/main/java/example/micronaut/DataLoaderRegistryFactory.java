package example.micronaut;

import io.micronaut.context.annotation.Factory;
import io.micronaut.runtime.http.scope.RequestScope;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Factory // <1>
public class DataLoaderRegistryFactory {

    private static final Logger LOG = LoggerFactory.getLogger(DataLoaderRegistryFactory.class);

    @SuppressWarnings("unused")
    @RequestScope // <2>
    public DataLoaderRegistry dataLoaderRegistry(AuthorDataLoader authorDataLoader) {
        DataLoaderRegistry dataLoaderRegistry = new DataLoaderRegistry();
        dataLoaderRegistry.register(
                "author",
                DataLoader.newMappedDataLoader(authorDataLoader)
        ); // <3>

        LOG.trace("Created new data loader registry");

        return dataLoaderRegistry;
    }

}