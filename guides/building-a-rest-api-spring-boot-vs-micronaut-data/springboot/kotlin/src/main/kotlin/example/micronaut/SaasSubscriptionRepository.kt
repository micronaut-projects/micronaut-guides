package example.micronaut

import org.springframework.data.repository.CrudRepository

interface SaasSubscriptionRepository : CrudRepository<SaasSubscription, Long>
