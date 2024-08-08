package example.micronaut;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import jakarta.inject.Inject;

@Controller("/hello") // <1>
public class HelloController {

    private HelloModule hello; // <2>

    @Inject // <3>
    public void inject(HelloModule hello) {
        this.hello = hello;
    }

    @Get // <4>
    @Produces(MediaType.TEXT_PLAIN) // <5>
    public String index() {
        return hello.hello("World"); // <6>
    }
}