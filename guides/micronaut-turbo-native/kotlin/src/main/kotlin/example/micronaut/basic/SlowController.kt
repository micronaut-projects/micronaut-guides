package example.micronaut.basic

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import example.micronaut.model.ViewModel
import io.micronaut.views.View
import java.util.concurrent.CompletableFuture

@Controller("/slow")
class SlowController {

    @View("slow")
    @Get(produces = [MediaType.TEXT_HTML], consumes = [MediaType.TEXT_HTML])
    fun index(): ViewModel = CompletableFuture.supplyAsync {
        Thread.sleep(3000)
        ViewModel("Slow-loading Page")
    }.get()
}