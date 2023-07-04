package example.micronaut.controllers;

import example.micronaut.models.Item;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import java.util.Optional;
import java.util.List;

@Controller("/items")  // <1>
@Secured(SecurityRule.IS_AUTHENTICATED)  // <2>
class ItemsController {

    @Get("/{id}")  // <3>
    public Optional<Item> findById(int id) {
        return Item.items.stream()
                .filter(it -> it.id().equals(id))
                .findFirst();
    }

    @Get  // <4>
    public List<Item> getItems() {
        return Item.items;
    }

}
