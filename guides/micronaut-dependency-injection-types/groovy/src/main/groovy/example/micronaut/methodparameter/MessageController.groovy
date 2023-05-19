package example.micronaut.methodparameter

import example.micronaut.MessageService
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import jakarta.inject.Inject

@Controller("/setter")
class MessageController {
    private MessageService messageService

    @Get
    @Produces(MediaType.TEXT_PLAIN)
    String index() {
        return messageService.compose()
    }

    @Inject
    void populateMessageService(MessageService messageService) {
        this.messageService = messageService
    }
}
