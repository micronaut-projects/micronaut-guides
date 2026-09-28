package example.micronaut;

import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.Micronaut;
import io.micronaut.scheduling.annotation.Async;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application implements ApplicationEventListener<StartupEvent> {  // <1>
    private static final Logger LOG = LoggerFactory.getLogger(Application.class);
    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }

    @Inject // <2>
    QuotersClient quotersClient;

    @Override
    public void onApplicationEvent(StartupEvent event) {  // <1>
        logQuote();
    }

    @Async // <3>
    void logQuote() {
        Quote quote = quotersClient.random();
        LOG.info("{}", quote);
    }
}