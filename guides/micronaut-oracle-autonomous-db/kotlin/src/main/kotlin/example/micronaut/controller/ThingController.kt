package example.micronaut.controller

import example.micronaut.domain.Thing
import example.micronaut.repository.ThingRepository
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import jakarta.validation.constraints.NotBlank

@Controller("/things")
@ExecuteOn(TaskExecutors.IO)
class ThingController(private val thingRepository: ThingRepository) {

    @Get
    fun all(): List<Thing> = thingRepository.findAll()

    @Get("/{name}")
    fun byName(name: @NotBlank String?) = thingRepository.findByName(name)
}
