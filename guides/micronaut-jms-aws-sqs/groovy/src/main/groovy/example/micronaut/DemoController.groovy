package example.micronaut

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller
class DemoController {

    private final DemoProducer demoProducer

    DemoController(DemoProducer demoProducer) {
        this.demoProducer = demoProducer
    }

    @Get("/demo")
    void publishDemoMessages() {
        demoProducer.send("Demo message body")
    }
}
