package example.micronaut.controller;

import example.micronaut.domain.Thing;
import example.micronaut.repository.ThingRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

@Controller("/things")
@ExecuteOn(TaskExecutors.IO)
class ThingController {

    private final ThingRepository thingRepository;

    ThingController(ThingRepository thingRepository) {
        this.thingRepository = thingRepository;
    }

    @Get
    List<Thing> all() {
        return thingRepository.findAll();
    }

    @Get("/{name}")
    Optional<Thing> byName(@NotBlank String name) {
        return thingRepository.findByName(name);
    }
}
