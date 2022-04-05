package example.micronaut;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Introspected
public class PetSave {
    @NonNull
    @NotBlank
    private final String name;

    @NonNull
    @NotNull
    private final PetType type;

    public PetSave(@NonNull String name,
                   @NonNull PetType type) {
        this.name = name;
        this.type = type;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public PetType getType() {
        return type;
    }
}
