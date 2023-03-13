package example.micronaut;

import io.micronaut.chatbots.basecamp.api.Query;
import io.micronaut.chatbots.basecamp.core.BasecampBotConfiguration;
import io.micronaut.chatbots.basecamp.core.BasecampHandler;
import jakarta.inject.Singleton;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Singleton
public class HelloWorldHandler implements BasecampHandler {
    @Override
    public boolean canHandle(BasecampBotConfiguration bot, @NotNull Query input) {
        return true;
    }

    @Override
    public Optional<String> handle(BasecampBotConfiguration bot, @NotNull Query input) {
        return Optional.of("Hello World");
    }
}
