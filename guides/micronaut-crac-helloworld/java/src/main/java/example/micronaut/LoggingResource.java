package example.micronaut;

import io.micronaut.crac.OrderedResource;
import jakarta.inject.Singleton;
import org.crac.Context;
import org.crac.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton // <1>
public class LoggingResource implements OrderedResource  {
    private static final Logger LOG = LoggerFactory.getLogger(LoggingResource.class);
    @Override
    public void beforeCheckpoint(Context<? extends Resource> context) throws Exception {
        LOG.info("before checkpoint");
    }

    @Override
    public void afterRestore(Context<? extends Resource> context) throws Exception {
        LOG.info("after restore");
    }
}
