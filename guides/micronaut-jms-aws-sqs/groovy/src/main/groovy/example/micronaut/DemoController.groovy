package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@CompileStatic
@Controller  // <1>
class DemoController {

    private final DemoProducer demoProducer

    DemoController(DemoProducer demoProducer) {  // <2>
        this.demoProducer = demoProducer
    }

    @Get("/demo")  // <3>
    void publishDemoMessages() {
        demoProducer.send("Demo message body")  // <4>
    }
}
