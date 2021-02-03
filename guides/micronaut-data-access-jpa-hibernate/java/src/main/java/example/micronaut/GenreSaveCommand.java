package example.micronaut;

import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.NotBlank;

@Introspected // <1>
public class GenreSaveCommand {

    @NotBlank
    private String name;

    public GenreSaveCommand() {
    }

    public GenreSaveCommand(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
