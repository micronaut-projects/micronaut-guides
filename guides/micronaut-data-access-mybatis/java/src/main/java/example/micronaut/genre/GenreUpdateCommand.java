package example.micronaut.genre;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.serde.annotation.Serdeable;

import jakarta.validation.constraints.NotBlank;

@Serdeable
public class GenreUpdateCommand {

    private long id;

    @NotBlank
    @NonNull
    private String name;

    public GenreUpdateCommand(long id, @NonNull @NotBlank String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }
}
