package example.micronaut;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Status;

@Controller  // <1>
public class DemoController {

    private final DemoProducer demoProducer;

    public DemoController(DemoProducer demoProducer) {  // <2>
        this.demoProducer = demoProducer;
    }

    @Post("/demo") // <3>
    @Status(HttpStatus.NO_CONTENT)
    public void publishDemoMessages() {
        demoProducer.send("Demo message body");  // <4>
    }
}