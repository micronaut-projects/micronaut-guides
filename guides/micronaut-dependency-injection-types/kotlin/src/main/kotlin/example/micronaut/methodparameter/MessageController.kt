package example.micronaut.methodparameter

import example.micronaut.MessageService
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import jakarta.inject.Inject

@Controller("/setter")
class MessageController {
    var messageService: MessageService? = null

    @Get
    @Produces(MediaType.TEXT_PLAIN)
    fun index() = messageService?.compose()

    @Inject
    fun populateMessageService(messageService: MessageService) {
        this.messageService = messageService
    }
}
