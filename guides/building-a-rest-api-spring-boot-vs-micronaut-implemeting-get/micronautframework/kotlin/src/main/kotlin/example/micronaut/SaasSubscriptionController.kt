package example.micronaut

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable

@Controller("/subscriptions") // <1>
class SaasSubscriptionController {

    @Get("/{id}") // <2>
    fun findById(@PathVariable id: Long): HttpResponse<SaasSubscription> { // <3>
        if (id == 99L) {
            val subscription = SaasSubscription(99L, "Advanced", 2900)
            return HttpResponse.ok(subscription)
        }
        return HttpResponse.notFound() // <4>
    }
}
