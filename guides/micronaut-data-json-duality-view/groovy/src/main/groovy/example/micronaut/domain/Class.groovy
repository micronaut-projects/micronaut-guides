package example.micronaut.domain

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import jakarta.validation.constraints.NotNull

@MappedEntity(value = "TBL_CLASS", alias = "c") // <1>
class Class {
    @Id // <2>
    @GeneratedValue(GeneratedValue.Type.IDENTITY) // <3>
    Long id

    @NotNull // <4>
    String name
}
