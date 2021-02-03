package example.micronaut.genre;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.NotBlank;

@Introspected
public class GenreSaveCommand {

    @NotBlank
    @NonNull
    private String name;

    public GenreSaveCommand() {
    }

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
