package example.micronaut;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

@Controller("/app") // <1>
class AppController {

    @Produces(MediaType.TEXT_PLAIN) // <2>
    @Secured(SecurityRule.IS_AUTHENTICATED) // <3>
    @Get // <4>
    String index() {
        return "Top Secret";
    }
}
