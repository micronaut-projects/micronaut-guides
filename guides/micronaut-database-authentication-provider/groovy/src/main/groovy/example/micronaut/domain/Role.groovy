package example.micronaut.domain

import io.micronaut.data.annotation.MappedEntity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.validation.constraints.NotBlank
import io.micronaut.serde.annotation.Serdeable

@Serdeable // <1>
@MappedEntity // <2>
class Role {
    @Id  // <3>
    @GeneratedValue // <4>
    Long id

    @NotBlank
    String authority
}
