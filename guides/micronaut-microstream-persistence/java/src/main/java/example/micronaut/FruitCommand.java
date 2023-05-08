package example.micronaut;

import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;

import jakarta.validation.constraints.NotBlank;

@Serdeable // <1>
public class FruitCommand {

    @NonNull
    @NotBlank // <2>
    private final String name;

    @Nullable // <3>
    private final String description;

    public FruitCommand(@NonNull String name) {
        this(name, null);
    }

    @Creator
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
