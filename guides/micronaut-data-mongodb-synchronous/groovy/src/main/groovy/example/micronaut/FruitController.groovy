package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.NonNull
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.annotation.Status
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn

import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull

@CompileStatic
@Controller("/fruits") // <1>
@ExecuteOn(TaskExecutors.IO) // <2>
class FruitController {

    private final FruitService fruitService

    FruitController(FruitService fruitService) {  // <3>
        this.fruitService = fruitService
    }

    @Get  // <4>
    Iterable<Fruit> list() {
        fruitService.list()
    }

    @Post // <5>
    @Status(HttpStatus.CREATED) // <6>
    Fruit save(@NonNull @NotNull @Valid Fruit fruit) { // <7>
        fruitService.save(fruit)
    }

    @Put
    Fruit update(@NonNull @NotNull @Valid Fruit fruit) {
        fruitService.save(fruit)
    }

    @Get("/{id}") // <8>
    Optional<Fruit> find(@PathVariable String id) {
        fruitService.find(id)
    }

    @Get("/q") // <9>
    Iterable<Fruit> query(@QueryValue @NotNull List<String> names) { // <10>
        fruitService.findByNameInList(names)
    }
}
