package example.micronaut.domain

import groovy.transform.CompileStatic
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.Reservable
import io.micronaut.serde.annotation.Serdeable

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.PositiveOrZero
import org.jspecify.annotations.Nullable

@CompileStatic
@Serdeable
@MappedEntity('ACCOUNT')
record Account(
    @Id @GeneratedValue @Nullable Long id,
    @NotBlank String name,
    @Reservable @PositiveOrZero Long balance,
    @Reservable @PositiveOrZero Long credit
) {
}
