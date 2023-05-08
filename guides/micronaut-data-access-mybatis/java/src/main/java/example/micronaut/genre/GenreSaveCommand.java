package example.micronaut.genre;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.serde.annotation.Serdeable;

import jakarta.validation.constraints.NotBlank;

@Serdeable
public class GenreSaveCommand {

    @NotBlank
    @NonNull
    private String name;

    public GenreSaveCommand(@NonNull @NotBlank String name) {
        this.name = name;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }
}
