package example.micronaut

import example.micronaut.domain.Thing
import example.micronaut.repository.ThingRepository
import io.micronaut.context.annotation.Requires
import io.micronaut.context.event.StartupEvent
import io.micronaut.runtime.event.annotation.EventListener
import jakarta.inject.Singleton
import jakarta.transaction.Transactional

@Singleton
@Requires(notEnv = ["test"])
open class DataPopulator(private val thingRepository: ThingRepository) {

    @EventListener
    @Transactional
    open fun init(event: StartupEvent) {
        // clear out any existing data
        thingRepository.deleteAll()

        // create data
        val fred = Thing("Fred")
        val barney = Thing("Barney")
        thingRepository.saveAll(listOf(fred, barney))
    }
}
