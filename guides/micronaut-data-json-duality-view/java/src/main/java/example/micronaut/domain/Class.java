package example.micronaut.domain;

import org.jspecify.annotations.Nullable;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

@MappedEntity(value = "TBL_CLASS", alias = "c")
public record Class (
    @Id
    @GeneratedValue(GeneratedValue.Type.IDENTITY)
    Long id,

    @NotNull
    String name
) {}
