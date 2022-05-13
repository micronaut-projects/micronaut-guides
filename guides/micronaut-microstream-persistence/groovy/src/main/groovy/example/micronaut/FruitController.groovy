package example.micronaut

import io.micronaut.core.annotation.NonNull
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.annotation.Status

import javax.validation.Valid
import javax.validation.constraints.NotNull

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

    @Post // <4>
    @Status(HttpStatus.CREATED) // <5>
    Fruit create(@NonNull @NotNull @Valid FruitCommand fruit) { // <6>
        fruitRepository.create(fruit)
    }

    @Put
    Fruit update(@NonNull @NotNull @Valid FruitCommand fruit) {
        fruitRepository.update(fruit)
    }

    @Get("/{name}") // <7>
    Fruit find(@PathVariable String name) {
        fruitRepository.find(name)
    }

    @Delete
    void delete(@NonNull @Valid FruitCommand fruit) {
        fruitRepository.delete(fruit)
    }
}
