package example.micronaut;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.context.event.StartupEvent;
import jakarta.inject.Singleton;

@Requires(property = "dynamodb-local.host")
@Requires(property = "dynamodb-local.port")
@Requires(env = Environment.DEVELOPMENT)
@Singleton
public class DevBootstrap implements ApplicationEventListener<StartupEvent> {

    private final IdGenerator idGenerator;
    private final BookRepository bookRepository;

    public DevBootstrap(IdGenerator idGenerator,
                     BookRepository bookRepository) {
        this.idGenerator = idGenerator;
        this.bookRepository = bookRepository;
    }

    @Override
    public void onApplicationEvent(StartupEvent event) {
        if (!bookRepository.existsTable()) {
            bookRepository.createTable();
            seedData();
        }
    }

    private void seedData(){
        bookRepository.save(new Book(idGenerator.generate(), "1680502395", "Release It!", 0));
        bookRepository.save(new Book(idGenerator.generate(), "0321601912", "Continuous Delivery", null));
        bookRepository.save(new Book(idGenerator.generate(), "1491950358", "Building Microservices", 4));
    }
}
