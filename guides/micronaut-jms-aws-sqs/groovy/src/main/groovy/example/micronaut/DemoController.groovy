package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Status

@CompileStatic
@Controller  // <1>
class DemoController {

    private final DemoProducer demoProducer

    DemoController(DemoProducer demoProducer) {  // <2>
        this.demoProducer = demoProducer
    }

    @Post("/demo")  // <3>
    @Status(HttpStatus.NO_CONTENT)
    void publishDemoMessages() {
        demoProducer.send("Demo message body")  // <4>
    }
}
