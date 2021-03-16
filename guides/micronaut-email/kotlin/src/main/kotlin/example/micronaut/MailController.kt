package example.micronaut

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import javax.validation.Valid

@Controller("/mail") // <1>
open class MailController(private val emailService: EmailService) {  // <2>

    @Post("/send") // <3>
    open fun send(@Body @Valid cmd: EmailCmd): HttpResponse<*> {  // <4>
        emailService.send(cmd)
        return HttpResponse.ok<Any>() // <5>
    }
}
