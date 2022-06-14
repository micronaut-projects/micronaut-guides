package example.micronaut;

import io.micronaut.context.LocalizedMessageSource;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

import java.util.Optional;

@Controller // <1>
public class HelloWorldController {

    private final LocalizedMessageSource messageSource;

    public HelloWorldController(LocalizedMessageSource messageSource) { // <2>
        this.messageSource = messageSource;
    }

    @Produces(MediaType.TEXT_PLAIN) // <3>
    @Get // <4>
    Optional<String> index() { // <5>
        return messageSource.getMessage("hello.world"); // <6>
    }
}
