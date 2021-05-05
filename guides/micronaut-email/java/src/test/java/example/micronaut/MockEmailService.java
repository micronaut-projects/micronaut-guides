package example.micronaut;

import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;

import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Primary
@Requires(property = "spec.name", value = "mailcontroller")
@Singleton
public class MockEmailService implements EmailService {

    public List<Email> emails = new ArrayList<>();

    @Override
    public void send(@NonNull @NotNull @Valid Email email) {
        emails.add(email);
    }
}
