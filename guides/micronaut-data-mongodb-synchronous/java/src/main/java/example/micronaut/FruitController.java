package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.annotation.Status;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;

@Controller("/fruits") // <1>
@ExecuteOn(TaskExecutors.IO)  // <2>
class FruitController {

    private final FruitService fruitService;

    FruitController(FruitService fruitService) {  // <3>
        this.fruitService = fruitService;
    }

    @Get  // <4>
    Iterable<Fruit> list() {
        return fruitService.list();
    }

    @Post // <5>
    @Status(HttpStatus.CREATED) // <6>
    Fruit save(@NonNull @NotNull @Valid Fruit fruit) { // <7>
        return fruitService.save(fruit);
    }

    @Put
    Fruit update(@NonNull @NotNull @Valid Fruit fruit) {
        return fruitService.save(fruit);
    }

    @Get("/{id}") // <8>
    Optional<Fruit> find(@PathVariable String id) {
        return fruitService.find(id);
    }

    @Get("/q") // <9>
    Iterable<Fruit> query(@QueryValue @NotNull List<String> names) { // <10>
        return fruitService.findByNameInList(names);
    }
}
