package example.micronaut.models

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import io.micronaut.core.annotation.Creator
import io.micronaut.core.annotation.Nullable
import io.micronaut.serde.annotation.Serdeable

import jakarta.validation.constraints.NotBlank

@CompileStatic
@EqualsAndHashCode
@Serdeable // <1>
class User {
    @Nullable Integer id // <2>

    @NotBlank @JsonProperty("first_name")
    String firstName

    @NotBlank @JsonProperty("last_name")
    String lastName

    @NotBlank
    String username

    @Creator
    User(Integer id,
         @NotBlank @JsonProperty("first_name") String firstName,
         @NotBlank @JsonProperty("last_name") String lastName,
         @NotBlank String username
    ) {
        this.id = id
        this.firstName = firstName
        this.lastName = lastName
        this.username = username
    }
}
