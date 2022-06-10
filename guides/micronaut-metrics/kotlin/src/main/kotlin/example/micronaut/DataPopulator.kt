package example.micronaut

import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment.TEST
import io.micronaut.context.event.StartupEvent
import io.micronaut.runtime.event.annotation.EventListener
import jakarta.inject.Singleton
import javax.transaction.Transactional

@Singleton // <1>
@Requires(notEnv = [TEST]) // <2>
open class DataPopulator(private val bookRepository: BookRepository) { // <3>

    @EventListener
    @Transactional
    open fun init(event: StartupEvent) {
        if (bookRepository.count() == 0L) {
            bookRepository.save(Book("1491950358", "Building Microservices"))
            bookRepository.save(Book("1680502395", "Release It!"))
            bookRepository.save(Book("0321601912", "Continuous Delivery"))
        }
    }
}
