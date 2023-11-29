package example.micronaut.controllers;

import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.views.fields.annotations.InputPassword;
import jakarta.validation.constraints.NotBlank;

@Serdeable // <1>
public record LoginForm(@NotBlank String username, // <2>
                        @InputPassword @NotBlank String password) {  // <3>
}
