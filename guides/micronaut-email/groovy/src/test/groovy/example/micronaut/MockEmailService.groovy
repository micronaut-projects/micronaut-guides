package example.micronaut

import io.micronaut.context.annotation.Primary
import io.micronaut.context.annotation.Requires
import io.micronaut.core.annotation.NonNull

import jakarta.inject.Singleton
import javax.validation.Valid
import javax.validation.constraints.NotNull

@Primary
@Requires(property = "spec.name", value = "mailcontroller")
@Singleton
class MockEmailService implements EmailService {

    public List<Email> emails = new ArrayList<>()

    @Override
    void send(@NonNull @NotNull @Valid Email email) {
        emails << email
    }
}
