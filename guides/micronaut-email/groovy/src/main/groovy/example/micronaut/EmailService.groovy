package example.micronaut

import edu.umd.cs.findbugs.annotations.NonNull

import javax.validation.Valid
import javax.validation.constraints.NotNull

interface EmailService {
    void send(@NonNull @NotNull @Valid Email email)
}