package example.micronaut;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller
public class DemoController {

    private final DemoProducer demoProducer;

    public DemoController(DemoProducer demoProducer) {
        this.demoProducer = demoProducer;
    }

    @Get("/demo")
    public void publishDemoMessages() {
        demoProducer.send("Demo message body");
    }

}
