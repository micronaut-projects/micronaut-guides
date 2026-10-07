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

    @GetMapping("/{id}") // <3>
    private ResponseEntity<SaasSubscription> findById(@PathVariable("id") Long id) { // <4>
        if (id == 99L) {
            SaasSubscription subscription = new SaasSubscription(99L, "Advanced", 2900)
            return ResponseEntity.ok(subscription)
        }
        ResponseEntity.notFound().build()
    }
}
