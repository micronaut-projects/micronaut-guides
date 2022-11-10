package example.micronaut;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller  // <1>
public class DemoController {

    private final DemoProducer demoProducer;

    public DemoController(DemoProducer demoProducer) {  // <2>
        this.demoProducer = demoProducer;
    }

    @Get("/demo")  // <3>
    public void publishDemoMessages() {
        demoProducer.send("Demo message body");  // <4>
    }

}
