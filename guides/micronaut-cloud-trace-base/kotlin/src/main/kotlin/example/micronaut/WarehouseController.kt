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
    fun getItemCount() : HttpResponse<Int> = HttpResponse.ok(Random().nextInt(11))


    @Post("/order") // <4>
    fun order() : HttpResponse<Any> {
        try {
            //To simulate an external process taking time
            Thread.sleep(500)
        } catch (e: InterruptedException) {
        }

        return HttpResponse.accepted()
    }

}