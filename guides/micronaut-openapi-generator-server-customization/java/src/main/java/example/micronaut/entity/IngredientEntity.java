package example.micronaut.entity;

import example.micronaut.model.MeasurementUnit;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;

@MappedEntity("ingredient")
public record IngredientEntity(
    @Nullable @Id @GeneratedValue(GeneratedValue.Type.AUTO) Long id,
    @ManyToOne @JoinColumn(name = "recipe_id", nullable = false) @Nullable RecipeEntity recipe, // <1>
    @NonNull @NotBlank String name,
    @NonNull float amount,
    @NonNull MeasurementUnit unit
) {
    public static IngredientEntity of(@NotBlank String name, @NonNull float amount, @NonNull MeasurementUnit unit) {
        return new IngredientEntity(null, null, name, amount, unit);
    }
}
