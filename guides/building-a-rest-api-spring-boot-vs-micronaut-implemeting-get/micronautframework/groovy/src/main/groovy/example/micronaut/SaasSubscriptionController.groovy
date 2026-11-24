package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable

@CompileStatic
@Controller("/subscriptions") // <1>
class SaasSubscriptionController {

    @Get("/{id}") // <2>
    HttpResponse<SaasSubscription> findById(@PathVariable Long id) { // <3>
        if (id == 99L) {
            SaasSubscription subscription = new SaasSubscription(99L, "Advanced", 2900)
            return HttpResponse.ok(subscription)
        }
        HttpResponse.notFound() // <4>
    }
}
