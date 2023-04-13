package example.micronaut.constructor

import example.micronaut.MessageService
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces

@Controller("/constructor")
class MessageController(val messageService: MessageService) {
    @Get
    @Produces(MediaType.TEXT_PLAIN)
    fun index() = messageService.compose()
}
