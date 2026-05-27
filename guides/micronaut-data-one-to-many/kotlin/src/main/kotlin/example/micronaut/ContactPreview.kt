package example.micronaut

import io.micronaut.core.annotation.Introspected

@Introspected // <1>
data class ContactPreview(
    val id: Long,
    val firstName: String?,
    val lastName: String?
)
