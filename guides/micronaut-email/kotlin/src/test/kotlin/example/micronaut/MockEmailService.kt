package example.micronaut

import io.micronaut.context.annotation.Primary
import io.micronaut.context.annotation.Requires
import java.util.ArrayList
import javax.inject.Singleton
import javax.validation.Valid
import javax.validation.constraints.NotNull

@Singleton
@Primary
@Requires(property = "spec.name", value = "mailcontroller")
open class MockEmailService : EmailService {

    var emails: MutableList<Email> = ArrayList()

    override fun send(email: @NotNull @Valid Email) {
        emails.add(email)
    }
}
