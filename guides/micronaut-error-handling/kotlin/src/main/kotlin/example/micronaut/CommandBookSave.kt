package example.micronaut

import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive

@Serdeable // <1>
data class CommandBookSave(
    @field:NotBlank val title: String, // <2>
    @field:Positive val pages: Int // <3>
)