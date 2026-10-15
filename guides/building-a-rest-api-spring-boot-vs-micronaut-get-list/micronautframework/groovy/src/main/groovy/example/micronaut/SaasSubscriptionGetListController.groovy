package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.Sort
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@CompileStatic
@Controller('/subscriptions') // <1>
class SaasSubscriptionGetListController {

    private static final Sort CENTS = Sort.of(Sort.Order.asc('cents'))
    private final SaasSubscriptionRepository repository

    SaasSubscriptionGetListController(SaasSubscriptionRepository repository) { // <2>
        this.repository = repository
    }

    @Get // <3>
    Iterable<SaasSubscription> findAll(Pageable pageable) { // <4>
        Page<SaasSubscription> page = repository.findAll(pageable.sort.isSorted()
                ? pageable
                : Pageable.from(pageable.number, pageable.size, CENTS)
        ) // <5>
        page.content
    }
}
