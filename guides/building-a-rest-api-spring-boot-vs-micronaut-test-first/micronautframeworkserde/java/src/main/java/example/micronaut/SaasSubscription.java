package example.micronaut;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable // <1>
record SaasSubscription(Long id, String name, Integer cents) {
}