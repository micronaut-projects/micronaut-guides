package example.micronaut

import io.micronaut.core.annotation.NonNull
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.annotation.QueryValue

import javax.validation.Valid
import javax.validation.constraints.NotNull

@Controller("/fruits") // <1>
class FruitController {

    private final FruitService fruitService

    FruitController(FruitService fruitService) {  // <2>
        this.fruitService = fruitService
    }

    @Get  // <3>
    Iterable<Fruit> list() {
        fruitService.list()
    }

    @Post // <4>
    HttpResponse<Fruit> save(@NonNull @NotNull @Valid Fruit fruit) { // <7>
        HttpResponse.created(fruitService.save(fruit))
    }

    @Put // <5>
    Fruit update(@NonNull @NotNull @Valid Fruit fruit) {
        fruitService.save(fruit)
    }

    @Get("/{id}") // <6>
    Optional<Fruit> find(@PathVariable String id) {
        fruitService.find(id)
    }

    @Get("/q") // <7>
    Iterable<Fruit> query(@QueryValue @NotNull List<String> names) { // <8>
        fruitService.findByNameInList(names)
    }
}
