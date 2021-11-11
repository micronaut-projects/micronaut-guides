package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Status;
import io.micronaut.http.client.HttpClient;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Controller("/fruits") // <1>
public class FruitController {

    private final FruitRepository fruitService;

    public FruitController(FruitRepository fruitService) {  // <2>
        this.fruitService = fruitService;
    }

    @Get  // <3>
    public Publisher<Fruit> list() {
        return fruitService.list();
    }

    @Post // <4>
    public Mono<HttpStatus> save(@NonNull @NotNull @Valid Fruit fruit) { // <5>
        return fruitService.save(fruit) // <6>
                .map(added -> (added) ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }
}
