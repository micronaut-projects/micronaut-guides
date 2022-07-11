package example.micronaut.basic

import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Status

@Controller("/nonexistent")
class NonExistentController {

    @Status(HttpStatus.NOT_FOUND)
    @Get(produces = [MediaType.TEXT_HTML], consumes = [MediaType.TEXT_HTML])
    String index() {
        "Not Found"
    }
}