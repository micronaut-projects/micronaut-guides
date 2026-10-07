package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable

@CompileStatic
@Controller("/subscriptions") // <1>
class SaasSubscriptionController {

    private final SaasSubscriptionRepository repository

    SaasSubscriptionController(SaasSubscriptionRepository repository) {
        this.repository = repository
    }

    @Get("/{id}") // <2>
    HttpResponse<SaasSubscription> findById(@PathVariable("id") Long id) { // <3>
        Optional<SaasSubscription> subscription = repository.findById(id)
        subscription.isPresent() ? HttpResponse.ok(subscription.get()) : (HttpResponse<SaasSubscription>) HttpResponse.notFound() // <4>
    }
}
