package example.micronaut

import io.micronaut.core.annotation.Introspected
import io.micronaut.core.annotation.NonNull
import jakarta.validation.constraints.NotBlank

@Introspected
class Contact(
    @field:E164
    @field:NotBlank
    @field:NonNull
    val phone: String
)