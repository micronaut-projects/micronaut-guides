package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.Produces
import io.micronaut.security.annotation.Secured
import reactor.core.publisher.Mono

import static io.micronaut.http.MediaType.TEXT_PLAIN
import static io.micronaut.security.rules.SecurityRule.IS_AUTHENTICATED

@CompileStatic
@Controller('/user') // <1>
class UserController {

    private final UsernameFetcher usernameFetcher

    UserController(UsernameFetcher usernameFetcher) {  // <2>
        this.usernameFetcher = usernameFetcher
    }

    @Secured(IS_AUTHENTICATED)  // <3>
    @Produces(TEXT_PLAIN) // <4>
    @Get// <5>
    Mono<String> index(@Header('Authorization') String authorization) {  // <6>
        return usernameFetcher.findUsername(authorization)
    }
}
