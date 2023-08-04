package example.micronaut.entity;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.DateUpdated;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

import java.time.ZonedDateTime;
import java.util.Set;

@MappedEntity("recipe")
public record RecipeEntity(
    @Nullable @Id @GeneratedValue(GeneratedValue.Type.AUTO) Long id,
    @NonNull @NotBlank String name,
    @NonNull @NotBlank String content,
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL) // <1>
    Set<IngredientEntity> ingredients,
    @DateCreated
    ZonedDateTime dateCreated, // <2>
    @DateUpdated
    ZonedDateTime dateUpdated // <3>
) {
    public static RecipeEntity of(@NotBlank String name, @NotBlank String content, Set<IngredientEntity> ingredients) {
        return new RecipeEntity(null, name, content, ingredients, null, null);
    }
}
