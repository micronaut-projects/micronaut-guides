package example.micronaut

import io.micronaut.chatbots.basecamp.api.Query
import io.micronaut.chatbots.basecamp.core.BasecampBotConfiguration
import io.micronaut.chatbots.basecamp.core.BasecampHandler
import jakarta.inject.Singleton
import javax.validation.constraints.NotNull
import java.util.Optional

@Singleton
public class HelloWorldHandler implements BasecampHandler {
    override fun canHandle(BasecampBotConfiguration bot, @NotNull Query input) = true
    override fun handle(BasecampBotConfiguration bot, @NotNull Query input) = Optional.of("Hello World")
}
