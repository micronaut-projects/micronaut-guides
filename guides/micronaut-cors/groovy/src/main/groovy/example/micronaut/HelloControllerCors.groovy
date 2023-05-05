package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.cors.CrossOrigin

@CompileStatic
@Controller("/hellocors")
class HelloControllerCors {

    @CrossOrigin("http://127.0.0.1:8000") //<1>
    @Get
    @Produces(MediaType.TEXT_PLAIN)
    String index() {
        "Hello World"
    }
}
