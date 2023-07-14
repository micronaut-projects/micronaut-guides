package example.micronaut

import io.micronaut.core.annotation.NonNull
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.annotation.Status

import jakarta.validation.constraints.NotBlank

import static io.micronaut.scheduling.TaskExecutors.IO
import io.micronaut.scheduling.annotation.ExecuteOn

import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull

@Controller("/fruits") // <1>
class FruitController {

    private final FruitRepository fruitRepository

    FruitController(FruitRepository fruitRepository) {  // <2>
        this.fruitRepository = fruitRepository
    }

    @Get // <3>
    Collection<Fruit> list() {
        fruitRepository.list()
    }

    @ExecuteOn(IO)
    @Post // <4>
    @Status(HttpStatus.CREATED) // <5>
    Fruit create(@NonNull @NotNull @Valid @Body FruitCommand fruit) { // <6>
        fruitRepository.create(fruit)
    }

    @ExecuteOn(IO)
    @Put
    Fruit update(@NonNull @NotNull @Valid @Body FruitCommand fruit) {
        fruitRepository.update(fruit)
    }

    @Get("/{name}") // <7>
    Fruit find(@NonNull @NotBlank @PathVariable String name) {
        fruitRepository.find(name)
    }

    @ExecuteOn(IO)
    @Delete
    @Status(HttpStatus.NO_CONTENT)
    void delete(@NonNull @NotNull @Valid @Body FruitCommand fruit) {
        fruitRepository.delete(fruit)
    }
}
