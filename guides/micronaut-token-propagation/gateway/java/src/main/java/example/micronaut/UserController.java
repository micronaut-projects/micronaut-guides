package example.micronaut;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.annotation.Secured;
import reactor.core.publisher.Mono;

import static io.micronaut.http.MediaType.TEXT_PLAIN;
import static io.micronaut.security.rules.SecurityRule.IS_AUTHENTICATED;

@Controller("/user")
class UserController {

    private final UsernameFetcher usernameFetcher;

    UserController(UsernameFetcher usernameFetcher) {
        this.usernameFetcher = usernameFetcher;
    }

    @Secured(IS_AUTHENTICATED)
    @Produces(TEXT_PLAIN)
    @Get
    Mono<String> index() {
        return usernameFetcher.findUsername();
    }
}
