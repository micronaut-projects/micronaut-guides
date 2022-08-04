package example.micronaut;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.context.event.StartupEvent;
import jakarta.inject.Singleton;

@Requires(property = "dynamodb-local.host")
@Requires(property = "dynamodb-local.port")
@Requires(env = Environment.TEST)
@Singleton
public class TestBootstrap implements ApplicationEventListener<StartupEvent> {

    private final BookRepository bookRepository;

    public TestBootstrap(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void onApplicationEvent(StartupEvent event) {
        if (!bookRepository.existsTable()) {
            bookRepository.createTable();
        }
    }
}
