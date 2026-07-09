package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable

@CompileStatic
@Controller('/subscriptions') // <1>
class SaasSubscriptionController {

    private final SaasSubscriptionRepository repository

    SaasSubscriptionController(SaasSubscriptionRepository repository) {
        this.repository = repository
    }

    @Get('/{id}') // <2>
    HttpResponse<SaasSubscription> findById(@PathVariable Long id) { // <3>
        repository.findById(id)
                .map { SaasSubscription subscription -> HttpResponse.ok(subscription) }
                .orElseGet { HttpResponse.notFound() } // <4>
    }
}
