package example.micronaut;

import io.micronaut.serde.annotation.Serdeable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
