package example.micronaut;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import jakarta.annotation.PostConstruct;

import java.util.List;
import java.util.Optional;

@Controller
class RestApiDemoController {

    private final CoffeeRepository coffeeRepository;

    @PostConstruct
    void loadData() {
        coffeeRepository.saveAll(List.of(
                new Coffee("Café Cereza"),
                new Coffee("Café Ganador"),
                new Coffee("Café Lareño"),
                new Coffee("Café Três Pontas")
        ));
    }

    RestApiDemoController(CoffeeRepository coffeeRepository) {
        this.coffeeRepository = coffeeRepository;
    }

    @Get("/coffees")
    Iterable<Coffee> getCoffees() {
        return coffeeRepository.findAll();
    }

    @Get("/coffees/{id}")
    Optional<Coffee> getCoffeById(@PathVariable String id) {
        return coffeeRepository.findById(id);
    }

    @Post("/coffees")
    Coffee postCoffee(@Body Coffee coffee) {
        coffeeRepository.save(coffee);
        return coffee;
    }

    @Put("/coffees/{id}")
    HttpResponse<Coffee> putCoffee(@PathVariable String id, @Body Coffee coffee) {
        return coffeeRepository.existsById(id) ?
                HttpResponse.ok(coffeeRepository.update(coffee)) :
                HttpResponse.created(coffeeRepository.save(coffee));
    }

    @Delete("/coffees/{id}")
    @Status(HttpStatus.NO_CONTENT)
    void deleteCoffee(@PathVariable String id) {
        coffeeRepository.deleteById(id);
    }
}