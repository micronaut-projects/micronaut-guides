package example.micronaut;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;

@Controller("/mail") // <1>
public class MailController {

    private final EmailService emailService;

    public MailController(EmailService emailService) { // <2>
        this.emailService = emailService;
    }

    @Post("/send") // <3>
    public HttpResponse<?> send(@Body @Valid EmailCmd cmd) { // <4>
        emailService.send(cmd);
        return HttpResponse.ok();  // <5>
    }
}
