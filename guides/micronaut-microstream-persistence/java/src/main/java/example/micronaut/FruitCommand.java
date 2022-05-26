package example.micronaut;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;

import javax.validation.constraints.NotBlank;

@Introspected // <1>
public class FruitCommand {

    @NonNull
    @NotBlank // <2>
    private final String name;

    @Nullable // <3>
    private final String description;

    public FruitCommand(@NonNull String name) {
        this(name, null);
    }

    public FruitCommand(@NonNull String name,
                        @Nullable String description) {
        this.name = name;
        this.description = description;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @Nullable
    public String getDescription() {
        return description;
    }
}
