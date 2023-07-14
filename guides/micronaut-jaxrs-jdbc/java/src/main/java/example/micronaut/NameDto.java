package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.serde.annotation.Serdeable;

import jakarta.validation.constraints.NotBlank;

@Serdeable // <1>
public class NameDto {

    @NonNull
    @NotBlank
    private final String name;

    public NameDto(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getName() {
        return name;
    }
}