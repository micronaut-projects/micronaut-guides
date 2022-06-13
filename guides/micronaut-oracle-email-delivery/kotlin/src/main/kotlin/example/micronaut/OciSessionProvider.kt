package example.micronaut

import io.micronaut.context.annotation.Property
import io.micronaut.email.javamail.sender.MailPropertiesProvider
import io.micronaut.email.javamail.sender.SessionProvider
import jakarta.inject.Singleton
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.PasswordAuthentication
import javax.mail.Session

@Singleton // <1>
class OciSessionProvider(provider: MailPropertiesProvider,
                         @Property(name = "smtp.user") user: String, // <2>
                         @Property(name = "smtp.password") password: String) // <2>
    : SessionProvider {

    private val properties: Properties
    private val user: String
    private val password: String

    init {
        properties = provider.mailProperties()
        this.user = user
        this.password = password
    }

    override fun session(): Session =
        Session.getInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication() = PasswordAuthentication(user, password) // <3>
        })
}
