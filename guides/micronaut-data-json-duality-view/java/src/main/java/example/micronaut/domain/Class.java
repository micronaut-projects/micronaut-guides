package example.micronaut.domain;

import org.jspecify.annotations.Nullable;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

@MappedEntity(value = "TBL_CLASS", alias = "c") // <1>
public record Class (
    @Id // <2>
    @GeneratedValue(GeneratedValue.Type.IDENTITY) // <3>
    Long id,

    @NotNull // <4>
    String name
) {}
