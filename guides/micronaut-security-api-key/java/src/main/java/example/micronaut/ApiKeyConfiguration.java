package example.micronaut;

import io.micronaut.context.annotation.EachProperty;
import jakarta.annotation.Nonnull;

@EachProperty("api-keys") // <1>
public interface ApiKeyConfiguration {

    @Nonnull
    String getName();

    @Nonnull
    String getKey();
}
