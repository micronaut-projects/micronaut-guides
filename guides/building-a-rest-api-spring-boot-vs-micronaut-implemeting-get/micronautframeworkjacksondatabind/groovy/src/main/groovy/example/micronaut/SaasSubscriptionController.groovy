package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable

@CompileStatic
@Controller("/subscriptions") // <1>
class SaasSubscriptionController {

    @Get("/{id}") // <2>
    SaasSubscription findById(@PathVariable Long id) { // <3>
        if (id == 99L) {
            return new SaasSubscription(99L, "Advanced", 2900)
        }
        null // <4>
    }
}
