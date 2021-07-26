package example.micronaut;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import org.reactivestreams.Publisher;
import io.micronaut.core.async.annotation.SingleResult;

@Controller("/user") // <1>
public class UserController {

    private final UsernameFetcher usernameFetcher;

    public UserController(UsernameFetcher usernameFetcher) {  // <2>
        this.usernameFetcher = usernameFetcher;
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)  // <3>
    @Produces(MediaType.TEXT_PLAIN) // <4>
    @Get // <5>
    @SingleResult
    Publisher<String> index(@Header("Authorization") String authorization) {  // <6>
        return usernameFetcher.findUsername(authorization);
    }
}
