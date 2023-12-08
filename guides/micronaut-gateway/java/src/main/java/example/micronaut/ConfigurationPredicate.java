package example.micronaut;

import io.micronaut.core.annotation.Nullable;

public interface ConfigurationPredicate extends Predicate {
    @Nullable
    String getPath();
}
