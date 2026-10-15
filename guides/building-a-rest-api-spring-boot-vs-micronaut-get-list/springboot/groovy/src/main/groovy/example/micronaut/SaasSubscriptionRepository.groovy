package example.micronaut

import groovy.transform.CompileStatic
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository

@CompileStatic
interface SaasSubscriptionRepository extends CrudRepository<SaasSubscription, Long>, // <1>
        PagingAndSortingRepository<SaasSubscription, Long> { // <2>
}
