package example.micronaut.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

@Serdeable
public record User(
        @Nullable @Max(10000) Integer id,
        @NotBlank @JsonProperty("first_name") String firstName,
        @NotBlank @JsonProperty("last_name") String lastName,
        String username
) {
}