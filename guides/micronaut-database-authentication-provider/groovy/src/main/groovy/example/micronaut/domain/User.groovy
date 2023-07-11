package example.micronaut.domain

import example.micronaut.UserState
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.serde.annotation.Serdeable
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.validation.constraints.NotBlank

@Serdeable  // <1>
@MappedEntity // <2>
class User implements UserState {
    @Id // <3>
    @GeneratedValue // <4>
    Long id

    @NotBlank
    String email

    @NotBlank
    String username

    @NotBlank
    String password

    boolean enabled
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired
}
