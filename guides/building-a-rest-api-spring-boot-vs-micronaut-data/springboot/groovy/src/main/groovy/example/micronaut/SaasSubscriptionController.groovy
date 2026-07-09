package example.micronaut

import groovy.transform.CompileStatic
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CompileStatic
@RestController // <1>
@RequestMapping("/subscriptions") // <2>
class SaasSubscriptionController {

    private final SaasSubscriptionRepository repository

    SaasSubscriptionController(SaasSubscriptionRepository repository) {
        this.repository = repository
    }

    @GetMapping("/{id}") // <3>
    ResponseEntity<SaasSubscription> findById(@PathVariable("id") Long id) { // <4>
        Optional<SaasSubscription> subscription = repository.findById(id)
        subscription.isPresent() ? ResponseEntity.ok(subscription.get()) : (ResponseEntity<SaasSubscription>) ResponseEntity.notFound().build()
    }
}
