package example.micronaut;

import io.micronaut.serde.annotation.Serdeable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Serdeable // <1>
public class GenreUpdateCommand {
    @NotNull
    private final Long id;

    @NotBlank
    private final String name;

    public GenreUpdateCommand(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
