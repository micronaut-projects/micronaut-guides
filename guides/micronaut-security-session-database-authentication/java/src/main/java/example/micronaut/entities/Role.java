package example.micronaut.entities;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import jakarta.validation.constraints.NotBlank;

@MappedEntity // <1>
public record Role(@Nullable
                   @Id // <2>
                   @GeneratedValue // <3>
                   Long id,
                   @NotBlank // <4>
                   String authority) {
}
