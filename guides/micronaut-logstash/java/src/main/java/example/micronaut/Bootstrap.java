package example.micronaut;

import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.context.event.StartupEvent;
import javax.inject.Singleton;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@Singleton // <1>
class Bootstrap implements ApplicationEventListener<StartupEvent> {  // <2>
    private static final Logger LOG = LoggerFactory.getLogger(Bootstrap.class);

    @Override
    public void onApplicationEvent(StartupEvent event) {
        LOG.info("application started");
    }
}