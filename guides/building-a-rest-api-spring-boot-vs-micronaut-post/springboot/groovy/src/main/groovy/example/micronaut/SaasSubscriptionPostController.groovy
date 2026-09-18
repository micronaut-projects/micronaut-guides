package example.micronaut

import groovy.transform.CompileStatic
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder

@CompileStatic
@RestController // <1>
@RequestMapping('/subscriptions') // <2>
class SaasSubscriptionPostController {

    private final SaasSubscriptionRepository repository

    private SaasSubscriptionPostController(SaasSubscriptionRepository repository) { // <3>
        this.repository = repository
    }

    @PostMapping // <4>
    private ResponseEntity<Void> createSaasSubscription(@RequestBody SaasSubscription newSaasSubscription, // <5>
                                                        UriComponentsBuilder ucb) {
        SaasSubscription subscription = repository.save(newSaasSubscription)
        URI locationOfSubscription = ucb
                .path('subscriptions/{id}')
                .buildAndExpand(subscription.id)
                .toUri()
        ResponseEntity.created(locationOfSubscription).build()
    }
}
