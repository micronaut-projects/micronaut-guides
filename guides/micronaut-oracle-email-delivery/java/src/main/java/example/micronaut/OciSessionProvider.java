package example.micronaut;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.email.javamail.sender.MailPropertiesProvider;
import io.micronaut.email.javamail.sender.SessionProvider;
import jakarta.inject.Singleton;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import java.util.Properties;

@Singleton // <1>
class OciSessionProvider implements SessionProvider {

    private final Properties properties;
    private final String user;
    private final String password;

    OciSessionProvider(MailPropertiesProvider provider,
                       @Property(name = "smtp.user") String user, // <2>
                       @Property(name = "smtp.password") String password) { // <2>
        this.properties = provider.mailProperties();
        this.user = user;
        this.password = password;
    }

    @Override
    @NonNull
    public Session session() {
        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password); // <3>
            }
        });
    }
}
