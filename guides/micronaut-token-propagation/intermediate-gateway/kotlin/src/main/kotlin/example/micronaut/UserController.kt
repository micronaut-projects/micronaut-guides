package example.micronaut

import io.micronaut.http.MediaType.TEXT_PLAIN
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.Produces
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule.IS_AUTHENTICATED
import reactor.core.publisher.Mono

@Controller("/user") // <1>
class UserController(private val usernameFetcher: UsernameFetcher) { // <2>

    @Secured(IS_AUTHENTICATED) // <3>
    @Produces(TEXT_PLAIN) // <4>
    @Get // <5>
    fun index(@Header("Authorization") authorization: String): Mono<String> = // <6>
            usernameFetcher.findUsername(authorization)
}
