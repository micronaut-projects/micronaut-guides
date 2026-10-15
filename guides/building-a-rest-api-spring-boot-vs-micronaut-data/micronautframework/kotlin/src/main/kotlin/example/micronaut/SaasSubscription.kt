package example.micronaut

import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.serde.annotation.Serdeable

@Serdeable // <1>
@MappedEntity // <2>
data class SaasSubscription(
    @field:Id val id: Long, // <3>
    val name: String,
    val cents: Int
)
