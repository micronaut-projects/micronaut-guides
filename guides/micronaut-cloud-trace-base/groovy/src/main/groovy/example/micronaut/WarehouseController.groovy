package example.micronaut

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn

import java.util.Random


@ExecuteOn(TaskExecutors.IO) // <1>
@Controller("/warehouse") // <2>
class WarehouseController {

    @Get("/count") // <3>
    HttpResponse getItemCount() {
        HttpResponse.ok(new Random().nextInt(10)+1)
    }

    @Post("/order") // <4>
    HttpResponse order() {
        try {
            //To simulate an external process taking time
            Thread.sleep(500)
        } catch (ex) {
        }

        HttpResponse.accepted()
    }
}