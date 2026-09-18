package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.uri.UriBuilder

@CompileStatic
@Controller('/subscriptions') // <1>
class SaasSubscriptionPostController {

    private final SaasSubscriptionRepository repository

    SaasSubscriptionPostController(SaasSubscriptionRepository repository) { // <2>
        this.repository = repository
    }

    @Post // <3>
    HttpResponse<?> createSaasSubscription(@Body SaasSubscription newSaasSubscription) { // <4>
        SaasSubscription savedSaasSubscription = repository.save(newSaasSubscription)
        URI locationOfNewSaasSubscription = UriBuilder.of('/subscriptions') // <5>
                .path(savedSaasSubscription.id.toString())
                .build()
        HttpResponse.created(locationOfNewSaasSubscription)
    }
}
