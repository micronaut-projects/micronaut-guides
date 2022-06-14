package example.micronaut

import io.micronaut.http.MediaType.TEXT_PLAIN
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule.IS_AUTHENTICATED
import reactor.core.publisher.Mono

@Controller("/user")
class UserController(private val usernameFetcher: UsernameFetcher) {

    @Secured(IS_AUTHENTICATED)
    @Produces(TEXT_PLAIN)
    @Get
    fun index(): Mono<String> = usernameFetcher.findUsername()
}
