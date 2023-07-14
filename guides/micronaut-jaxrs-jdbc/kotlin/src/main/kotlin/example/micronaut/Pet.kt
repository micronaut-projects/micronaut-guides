package example.micronaut

import io.micronaut.data.annotation.MappedEntity
import io.micronaut.serde.annotation.Serdeable
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import jakarta.validation.constraints.NotBlank

@Serdeable // <1>
@MappedEntity // <2>
data class Pet(@NotBlank val name: String,
               val type: PetType = PetType.DOG) {
    @Id // <3>
    @GeneratedValue(GeneratedValue.Type.AUTO) var id: Long? = null // <4>
}