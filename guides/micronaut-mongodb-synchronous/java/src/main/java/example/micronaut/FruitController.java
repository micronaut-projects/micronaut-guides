package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Status;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Controller("/fruits") // <1>
public class FruitController {

    private final FruitRepository fruitService;

    public FruitController(FruitRepository fruitService) {  // <2>
        this.fruitService = fruitService;
    }

    @ExecuteOn(TaskExecutors.IO)  // <3>
    @Get  // <4>
    public List<Fruit> list() {
        return fruitService.list();
    }

    @ExecuteOn(TaskExecutors.IO)  // <3>
    @Post // <5>
    @Status(HttpStatus.CREATED) // <6>
    public void save(@NonNull @NotNull @Valid Fruit fruit) { // <7>
        fruitService.save(fruit);
    }
}
