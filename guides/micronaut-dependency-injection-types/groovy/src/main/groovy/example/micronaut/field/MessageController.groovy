package example.micronaut.field

import example.micronaut.MessageService
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import jakarta.inject.Inject

@Controller("/field")
class MessageController {
    @Inject
    MessageService messageService

    @Get
    @Produces(MediaType.TEXT_PLAIN)
    String index() {
        messageService.compose()
    }
}
