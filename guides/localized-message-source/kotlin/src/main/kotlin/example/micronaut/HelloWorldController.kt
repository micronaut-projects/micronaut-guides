package example.micronaut

import io.micronaut.context.LocalizedMessageSource
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import java.util.Optional

@Controller // <1>
class HelloWorldController(private val messageSource: LocalizedMessageSource) { // <2>

    @Produces(MediaType.TEXT_PLAIN) // <3>
    @Get// <4>
    fun index(): Optional<String> { // <5>
        return messageSource.getMessage("hello.world") // <6>
    }
}
