package example.micronaut;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Serdeable // <1>
public record Option(@NotNull Language language, // <2>
                     @NotNull BuildTool buildTool, // <2>
                     @NotBlank String url) { // <2>
}
