package example.micronaut.genre;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Introspected
public class GenreUpdateCommand {

    @NotNull
    @NonNull
    private Long id;

    @NotBlank
    @NonNull
    private String name;

    public GenreUpdateCommand() {
    }

    public GenreUpdateCommand(@NonNull @NotNull Long id, @NonNull @NotBlank String name) {
        this.id = id;
        this.name = name;
    }

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
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
