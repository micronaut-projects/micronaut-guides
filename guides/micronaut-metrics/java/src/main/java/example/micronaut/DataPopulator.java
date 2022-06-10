package example.micronaut;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;

import javax.transaction.Transactional;

import static io.micronaut.context.env.Environment.TEST;

@Singleton // <1>
@Requires(notEnv = TEST) // <2>
class DataPopulator {

    private final BookRepository bookRepository;

    DataPopulator(BookRepository bookRepository) { // <3>
        this.bookRepository = bookRepository;
    }

    @EventListener
    @Transactional
    void init(StartupEvent event) {
        if (bookRepository.count() == 0) {
            bookRepository.save(new Book("1491950358", "Building Microservices"));
            bookRepository.save(new Book("1680502395", "Release It!"));
            bookRepository.save(new Book("0321601912", "Continuous Delivery"));
        }
    }
}
