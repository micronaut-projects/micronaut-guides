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
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller("/fruits") // <1>
@ExecuteOn(TaskExecutors.IO)  // <2>
public class FruitController {

    private final FruitRepository fruitService;

    public FruitController(FruitRepository fruitService) {  // <3>
        this.fruitService = fruitService;
    }

    @Get  // <4>
    public List<Fruit> list() {
        return StreamSupport.stream(fruitService.list().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Post // <5>
    @Status(HttpStatus.CREATED) // <6>
    public void save(@NonNull @NotNull @Valid Fruit fruit) { // <7>
        fruitService.save(fruit);
    }
}
