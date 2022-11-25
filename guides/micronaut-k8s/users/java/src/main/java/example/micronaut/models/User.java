package example.micronaut.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

@Serdeable
public record User(
        @Max(10000) Integer id, // <1>
        @NotBlank @JsonProperty("first_name") String firstName,
        @NotBlank @JsonProperty("last_name") String lastName,
        String username
) {
}