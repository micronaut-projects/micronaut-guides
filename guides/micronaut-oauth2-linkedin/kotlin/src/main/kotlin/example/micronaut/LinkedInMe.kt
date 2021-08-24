package example.micronaut

import io.micronaut.core.annotation.Creator
import io.micronaut.core.annotation.Introspected
import io.micronaut.core.annotation.NonNull
import javax.validation.constraints.NotBlank

@Introspected
data class LinkedInMe @Creator constructor(
    @field:NonNull @field:NotBlank @get:NonNull @param:NonNull val id: String,
    @field:NonNull @field:NotBlank @get:NonNull @param:NonNull val localizedFirstName: String,
    @field:NonNull @field:NotBlank @get:NonNull @param:NonNull val localizedLastName: String)
