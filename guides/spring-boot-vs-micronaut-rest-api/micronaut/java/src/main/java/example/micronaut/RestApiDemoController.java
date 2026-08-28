package example.micronaut;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
class RestApiDemoController {
    private List<Coffee> coffees = new ArrayList<>();

    public RestApiDemoController() {
        coffees.addAll(List.of(
                new Coffee("Café Cereza"),
                new Coffee("Café Ganador"),
                new Coffee("Café Lareño"),
                new Coffee("Café Três Pontas")
        ));
    }

    @Get("/coffees")
    Iterable<Coffee> getCoffees() {
        return coffees;
    }

    @Get("/coffees/{id}")
    Optional<Coffee> getCoffeById(@PathVariable String id) {
        return coffees.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    @Post("/coffees")
    Coffee postCoffee(@Body Coffee coffee) {
        coffees.add(coffee);
        return coffee;
    }

    @Put("/coffees/{id}")
    HttpResponse<Coffee> putCoffee(@PathVariable String id, @Body Coffee coffee) {
        int coffeeIndex = - 1;
        for (Coffee c : coffees) {
            if (c.getId().equals(id)) {
                coffeeIndex = coffees.indexOf(c);
                coffees.set(coffeeIndex, coffee);
            }
        }
        return (coffeeIndex == -1 ?
                HttpResponse.created(postCoffee(coffee)) :
                HttpResponse.ok(coffee));
    }

    @Delete("/coffees/{id}")
    @Status(HttpStatus.NO_CONTENT)
    void deleteCoffee(@PathVariable String id) {
        coffees.removeIf(c -> c.getId().equals(id));
    }
}
