package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Status;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

import static io.micronaut.http.HttpStatus.CREATED;

@Controller("/fruits") // <1>
@ExecuteOn(TaskExecutors.IO)  // <2>
class FruitController {

    private final FruitRepository fruitService;

    FruitController(FruitRepository fruitService) {  // <3>
        this.fruitService = fruitService;
    }

    @Get  // <4>
    List<Fruit> list() {
        return fruitService.list();
    }

    @Post // <5>
    @Status(CREATED) // <6>
    void save(@NonNull @NotNull @Valid Fruit fruit) { // <7>
        fruitService.save(fruit);
    }
}
