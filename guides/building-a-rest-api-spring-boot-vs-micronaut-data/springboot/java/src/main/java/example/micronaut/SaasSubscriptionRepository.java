package example.micronaut;

import org.springframework.data.repository.CrudRepository;

interface SaasSubscriptionRepository extends CrudRepository<SaasSubscription, Long> {
}