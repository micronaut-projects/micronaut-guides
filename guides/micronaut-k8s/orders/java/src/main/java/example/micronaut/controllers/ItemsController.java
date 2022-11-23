package example.micronaut.controllers;

import example.micronaut.models.Item;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import javax.validation.constraints.NotNull;
import java.util.List;

@Controller("/items")  // <1>
@Secured(SecurityRule.IS_AUTHENTICATED)  // <2>
public class ItemsController {

    @Get("/{id}")  // <3>
    public Item findById(@NotNull Integer id) {
        return Item.items.stream()
                .filter(it -> it.id().equals(id))
                .findFirst().orElse(null);
    }

    @Get  // <4>
    public List<Item> getItems() {
        return Item.items;
    }

}
