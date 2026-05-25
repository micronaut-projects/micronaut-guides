package example.micronaut

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable

@Controller("/subscriptions") // <1>
class SaasSubscriptionController {

    @Get("/{id}") // <2>
    fun findById(@PathVariable id: Long): SaasSubscription? { // <3>
        if (id == 99L) {
            return SaasSubscription(99L, "Advanced", 2900)
        }
        return null // <4>
    }
}
