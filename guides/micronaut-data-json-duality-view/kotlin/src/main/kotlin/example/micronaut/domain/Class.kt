package example.micronaut.domain

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import jakarta.validation.constraints.NotNull

@MappedEntity(value = "TBL_CLASS", alias = "c") // <1>
data class Class(
    @Id // <2>
    @GeneratedValue(GeneratedValue.Type.IDENTITY) // <3>
    val id: Long?,
    @field:NotNull // <4>
    val name: String
)
