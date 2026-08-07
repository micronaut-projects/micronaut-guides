package example.micronaut;

import io.micronaut.context.annotation.Value;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

@Controller("/greeting")
class GreetingController {

    @Value("${greeting-name:Mirage}")
    protected String name;

    @Value("${greeting-coffee:${greeting-name} is drinking Café Ganador}")
    protected String coffee;

    @Produces(MediaType.TEXT_PLAIN)
    @Get
    String getGreeting() {
        return name;
    }

    @Produces(MediaType.TEXT_PLAIN)
    @Get("/coffee")
    String getCoffee() {
        return coffee;
    }



}
