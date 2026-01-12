package example.micronaut;

import org.jspecify.annotations.NonNull;
import org.stringtemplate.v4.ST;

import java.util.Optional;
import java.util.function.Consumer;

public interface PromptLoader {
    @NonNull Optional<String> prompt(@NonNull String promptTemplatePath, Consumer<ST> promptTemplateConsumer);
}
