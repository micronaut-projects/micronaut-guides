package example.micronaut

import groovy.transform.CompileStatic
import org.springframework.data.repository.CrudRepository

@CompileStatic
interface SaasSubscriptionRepository extends CrudRepository<SaasSubscription, Long> {
}
