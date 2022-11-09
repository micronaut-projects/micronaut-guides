package example.micronaut

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller
class DemoController(private val demoProducer: DemoProducer) {

    @Get("/demo")
    fun publishDemoMessages() {
        demoProducer.send("Demo message body")
    }
}