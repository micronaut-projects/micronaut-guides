package example.micronaut;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Serdeable // <1>
public record Guide(@NotBlank String title,
                    @NotBlank String intro,
                    @NotNull @Size(min = 1) List<@NotBlank String> authors, // <2>
                    @Nullable List<@NotBlank String> tags,
                    @NotNull @Size(min = 1) List<@NotBlank String> categories,
                    @Schema(format = "yyyy-MM-dd", example = "2018-05-23") @NotNull LocalDate publicationDate, // <3>
                    @NotBlank String slug,
                    @NotBlank String url,
                    @NotNull @Size(min = 1) List<@NotNull @Valid Option> options) {
}
