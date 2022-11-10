package example.micronaut

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller  // <1>
class DemoController(private val demoProducer: DemoProducer) {  // <2>

    @Get("/demo")  // <3>
    fun publishDemoMessages() {
        demoProducer.send("Demo message body")  // <4>
    }
}