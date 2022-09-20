package example.micronaut.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
class Genre(@Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            val id: Long?,

            @NotBlank
            var name: String
)