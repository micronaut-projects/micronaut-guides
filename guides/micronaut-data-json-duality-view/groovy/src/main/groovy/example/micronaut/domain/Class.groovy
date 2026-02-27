package example.micronaut.domain

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import jakarta.validation.constraints.NotNull

@MappedEntity(value = "TBL_CLASS", alias = "c")
class Class {
    @Id
    @GeneratedValue(GeneratedValue.Type.IDENTITY)
    Long id

    @NotNull
    String name
}
