package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static io.micronaut.http.HttpStatus.CONFLICT;
import static io.micronaut.http.HttpStatus.CREATED;

@Controller("/fruits") // <1>
class FruitController {

    private final FruitRepository fruitService;

    FruitController(FruitRepository fruitService) {  // <2>
        this.fruitService = fruitService;
    }

    @Get // <3>
    Publisher<Fruit> list() {
        return fruitService.list();
    }

    @Post // <4>
    Mono<HttpStatus> save(@NonNull @NotNull @Valid Fruit fruit) { // <5>
        return fruitService.save(fruit) // <6>
                .map(added -> added ? CREATED : CONFLICT);
    }
}
