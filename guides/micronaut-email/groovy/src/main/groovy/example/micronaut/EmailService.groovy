package example.micronaut

import io.micronaut.core.annotation.NonNull

import javax.validation.Valid
import javax.validation.constraints.NotNull

interface EmailService {
    void send(@NonNull @NotNull @Valid Email email)
}