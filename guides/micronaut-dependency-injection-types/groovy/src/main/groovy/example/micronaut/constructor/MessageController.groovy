package example.micronaut.constructor

import example.micronaut.MessageService
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces

@Controller("/constructor")
class MessageController {
    private final MessageService messageService

    MessageController(MessageService messageService) {
        this.messageService = messageService
    }

    @Get
    @Produces(MediaType.TEXT_PLAIN)
    String index() {
        messageService.compose()
    }
}
