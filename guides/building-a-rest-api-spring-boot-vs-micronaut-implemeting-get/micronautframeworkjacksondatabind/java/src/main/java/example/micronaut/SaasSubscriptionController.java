package example.micronaut;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
@Controller("/subscriptions") // <1>
class SaasSubscriptionController {
    @Get("/{id}") // <2>
    SaasSubscription findById(@PathVariable Long id) { // <3>
        if (id.equals(99L)) {
            return new SaasSubscription(99L, "Advanced", 2900);
        }
        return null; // <4>
    }
}
