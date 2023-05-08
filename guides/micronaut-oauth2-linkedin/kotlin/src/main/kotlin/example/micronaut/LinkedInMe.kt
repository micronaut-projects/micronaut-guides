package example.micronaut

import io.micronaut.core.annotation.NonNull
import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.NotBlank

@Serdeable
data class LinkedInMe(
    @field:NonNull @field:NotBlank @get:NonNull @param:NonNull val id: String,
    @field:NonNull @field:NotBlank @get:NonNull @param:NonNull val localizedFirstName: String,
    @field:NonNull @field:NotBlank @get:NonNull @param:NonNull val localizedLastName: String)
