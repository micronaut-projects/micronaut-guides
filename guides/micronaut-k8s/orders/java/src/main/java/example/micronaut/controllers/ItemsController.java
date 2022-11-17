package example.micronaut.controllers;

import example.micronaut.models.Item;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;

@Controller("/items")  // <1>
@Secured(SecurityRule.IS_AUTHENTICATED)  // <2>
public class ItemsController {

    @Get("/{id}")  // <3>
    public Mono<Item> findById(@NotNull Integer id) {
        return Mono.justOrEmpty(Item.items.stream()
                .filter(it -> it.getId().equals(id))
                .findFirst());
    }

    @Get  // <4>
    public Flux<Item> getItems() {
        return Flux.fromStream(Item.items.stream());
    }

}
