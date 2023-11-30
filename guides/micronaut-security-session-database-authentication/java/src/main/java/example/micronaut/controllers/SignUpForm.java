package example.micronaut.controllers;

import example.micronaut.constraints.PasswordMatch;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.views.fields.annotations.InputPassword;
import jakarta.validation.constraints.NotBlank;

@PasswordMatch // <1>
@Serdeable // <2>
public record SignUpForm(@NotBlank String username, // <3>
                         @InputPassword @NotBlank String password, // <4>
                         @InputPassword @NotBlank String repeatPassword) { // <4>
}
