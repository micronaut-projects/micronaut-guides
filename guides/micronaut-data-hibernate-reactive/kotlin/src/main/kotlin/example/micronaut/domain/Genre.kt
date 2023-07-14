package example.micronaut.domain

import io.micronaut.serde.annotation.Serdeable
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

@Serdeable
@Entity
class Genre(@Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            val id: Long?,

            @NotBlank
            var name: String
)