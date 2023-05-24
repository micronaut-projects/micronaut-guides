package example.micronaut.controllers

import example.micronaut.models.Item
import io.micronaut.core.annotation.NonNull
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule

@Controller("/items")  // <1>
@Secured(SecurityRule.IS_AUTHENTICATED)  // <2>
class ItemsController {

    @Get("/{id}")  // <3>
    Item findById(int id) {
        Item.items.stream()
                .filter(it -> it.id == id)
                .findFirst().orElse(null)
    }

    @Get  // <4>
    List<Item> getItems() {
        Item.items
    }
}
