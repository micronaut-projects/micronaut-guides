package example.micronaut.controllers

import example.micronaut.models.Item
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import javax.validation.constraints.NotNull

@Controller("/items") // <1>
@Secured(SecurityRule.IS_AUTHENTICATED) // <2>
class ItemsController {

    @Get("/{id}") // <3>
    fun findById(id: @NotNull Int?): Item? {
        return Item.items
            .firstOrNull { it.id == id }
    }

    @Get // <4>
    fun getItems(): List<Item?> {
        return Item.items
    }
}