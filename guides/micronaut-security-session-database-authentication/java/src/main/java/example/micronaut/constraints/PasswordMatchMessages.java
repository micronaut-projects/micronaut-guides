package example.micronaut.constraints;

import io.micronaut.context.StaticMessageSource;
import jakarta.inject.Singleton;

@Singleton // <1>
public class PasswordMatchMessages extends StaticMessageSource {

    public static final String PASSWORD_MATCH_MESSAGE = "Passwords do not match";

    private static final String MESSAGE_SUFFIX = ".message";

    public PasswordMatchMessages() {
        addMessage(PasswordMatch.class.getName() + MESSAGE_SUFFIX, PASSWORD_MATCH_MESSAGE);
    }
}
