package example.micronaut;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;

@Serdeable // <1>
public class GenreUpdateCommand {
    private final long id;

    @NotBlank
    private final String name;

    public GenreUpdateCommand(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
