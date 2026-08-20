package example.micronaut.domain

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.Reservable
import io.micronaut.serde.annotation.Serdeable

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.PositiveOrZero

@Serdeable
@MappedEntity("ACCOUNT")
data class Account(
    @field:Id
    @field:GeneratedValue
    val id: Long? = null,
    @field:NotBlank
    val name: String,
    @field:Reservable
    @field:PositiveOrZero
    val balance: Long,
    @field:Reservable
    @field:PositiveOrZero
    val credit: Long
)
