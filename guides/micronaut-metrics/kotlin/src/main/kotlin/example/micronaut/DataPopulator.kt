package example.micronaut

import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment.TEST
import io.micronaut.context.event.StartupEvent
import io.micronaut.runtime.event.annotation.EventListener
import jakarta.inject.Singleton
import jakarta.transaction.Transactional

@Singleton // <1>
open class DataPopulator(private val bookRepository: BookRepository) { // <2>

    @EventListener // <3>
    @Transactional // <4>
    open fun init(event: StartupEvent) {
        if (bookRepository.count() == 0L) {
            bookRepository.save(Book("1491950358", "Building Microservices"))
            bookRepository.save(Book("1680502395", "Release It!"))
            bookRepository.save(Book("0321601912", "Continuous Delivery"))
        }
    }
}
