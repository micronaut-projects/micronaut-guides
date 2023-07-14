package example.micronaut

import example.micronaut.domain.Thing
import example.micronaut.repository.ThingRepository
import groovy.transform.CompileStatic
import io.micronaut.context.annotation.Requires
import io.micronaut.context.event.StartupEvent
import io.micronaut.runtime.event.annotation.EventListener
import jakarta.inject.Singleton

import jakarta.transaction.Transactional

@Singleton
@Requires(notEnv = 'test')
@CompileStatic
class DataPopulator {

    private final ThingRepository thingRepository

    DataPopulator(ThingRepository thingRepository) {
        this.thingRepository = thingRepository
    }

    @EventListener
    @Transactional
    void init(StartupEvent event) {
        // clear out any existing data
        thingRepository.deleteAll()

        // create data
        Thing fred = new Thing('Fred')
        Thing barney = new Thing('Barney')
        thingRepository.saveAll([fred, barney])
    }
}
