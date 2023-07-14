package example.micronaut.controller

import example.micronaut.domain.Thing
import example.micronaut.repository.ThingRepository
import groovy.transform.CompileStatic
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn

import jakarta.validation.constraints.NotBlank

@Controller('/things')
@ExecuteOn(TaskExecutors.IO)
@CompileStatic
class ThingController {

    private final ThingRepository thingRepository

    ThingController(ThingRepository thingRepository) {
        this.thingRepository = thingRepository
    }

    @Get
    List<Thing> all() {
        thingRepository.findAll()
    }

    @Get('/{name}')
    Optional<Thing> byName(@NotBlank String name) {
        thingRepository.findByName(name)
    }
}
