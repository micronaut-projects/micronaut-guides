package example.micronaut;

import io.micronaut.context.StaticMessageSource;
import jakarta.inject.Singleton;

/**
 * Adds validation messages.
 */
@Singleton
public class CustomValidationMessages extends StaticMessageSource {
    public static final String E164_MESSAGE = "must be a phone in E.164 format";
    /**
     * The message suffix to use.
     */
    private static final String MESSAGE_SUFFIX = ".message";

    /**
     * Default constructor to initialize messages.
     * via {@link #addMessage(String, String)}
     */
    public CustomValidationMessages() {
        addMessage(E164.class.getName() + MESSAGE_SUFFIX, E164_MESSAGE);
    }
}
