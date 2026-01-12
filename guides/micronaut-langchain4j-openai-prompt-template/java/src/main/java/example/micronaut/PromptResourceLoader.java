package example.micronaut;

import io.micronaut.context.exceptions.ConfigurationException;
import io.micronaut.core.annotation.Internal;
import io.micronaut.core.io.ResourceLoader;
import jakarta.inject.Singleton;
import org.jspecify.annotations.NonNull;
import org.stringtemplate.v4.ST;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.function.Consumer;

@Singleton
@Internal
class PromptResourceLoader implements PromptLoader {
    private final ResourceLoader resourceLoader;

    PromptResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public @NonNull Optional<String> prompt(@NonNull String promptTemplatePath, Consumer<ST> promptTemplateConsumer) {
        Optional<InputStream> isOpt = resourceLoader.getResourceAsStream(promptTemplatePath);
        if (isOpt.isEmpty()) {
            return Optional.empty();
        }
        try (InputStream inputStream = isOpt.get()) {
            String template = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            ST st = new ST(template, '{', '}');
            if (promptTemplateConsumer != null) {
                promptTemplateConsumer.accept(st);
            }
            return Optional.of(st.render());
        } catch (IOException e) {
            throw new ConfigurationException("Could not load prompt template " + promptTemplatePath, e);
        }
    }
}
