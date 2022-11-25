package example.micronaut

import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Status

@Controller  // <1>
class DemoController(private val demoProducer: DemoProducer) {  // <2>

    @Post("/demo")  // <3>
    @Status(HttpStatus.NO_CONTENT)
    fun publishDemoMessages() {
        demoProducer.send("Demo message body")  // <4>
    }
}