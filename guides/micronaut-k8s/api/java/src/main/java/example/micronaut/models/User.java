package example.micronaut.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;

@Serdeable // <1>
public record User(
        @Nullable @Max(10000) Integer id,
        @NotBlank @JsonProperty("first_name") String firstName,
        @NotBlank @JsonProperty("last_name") String lastName,
        String username
) {
}