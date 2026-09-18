package example.micronaut;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

@Controller("/greet")
class GreetingConfigurationPropertiesController {

    private final Greeting greeting;

    GreetingConfigurationPropertiesController(Greeting greeting) {
        this.greeting = greeting;
    }

    @Produces(MediaType.TEXT_PLAIN)
    @Get("/name")
    String getGreeting() {
        return greeting.getName();
    }

    @Produces(MediaType.TEXT_PLAIN)
    @Get("/coffee")
    String getCoffee() {
        return greeting.getCoffee();
    }



}
