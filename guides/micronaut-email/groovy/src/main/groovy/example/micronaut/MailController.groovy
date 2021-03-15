package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post

import javax.validation.Valid

@CompileStatic
@Controller("/mail") // <1>
class MailController {

    private final EmailService emailService

    MailController(EmailService emailService) { // <2>
        this.emailService = emailService
    }

    @Post("/send") // <3>
    HttpResponse<?> send(@Body @Valid EmailCmd cmd) { // <4>
        emailService.send(cmd)
        HttpResponse.ok()  // <5>
    }
}
