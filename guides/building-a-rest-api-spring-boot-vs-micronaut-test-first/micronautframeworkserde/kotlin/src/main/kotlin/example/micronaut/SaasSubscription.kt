package example.micronaut

import io.micronaut.serde.annotation.Serdeable

@Serdeable // <1>
data class SaasSubscription(val id: Long, val name: String, val cents: Int)
