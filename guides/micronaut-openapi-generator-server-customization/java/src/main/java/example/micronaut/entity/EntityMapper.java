package example.micronaut.entity;

import example.micronaut.model.Ingredient;
import example.micronaut.model.Quantity;
import example.micronaut.model.Recipe;
import jakarta.inject.Singleton;

import java.util.stream.Collectors;

/**
 * A class for mapping entity types to the models and back.
 */
@Singleton
public class EntityMapper {

    /**
     * Create {@link IngredientEntity} from {@link Ingredient}.
     * @param ingredient the ingredient
     * @return the ingredient entity
     */
    public IngredientEntity ingredientToEntity(Ingredient ingredient) { // <1>
        return new IngredientEntity(
                null,
                null,
                ingredient.getName(),
                ingredient.getQuantity().getAmount(),
                ingredient.getQuantity().getUnit()
        );
    }

    /**
     * Create {@link Ingredient} from {@link IngredientEntity}.
     * @param entity the ingredient entity
     * @return the ingredient
     */
    public Ingredient ingredientFromEntity(IngredientEntity entity) { // <2>
        return new Ingredient(
                entity.name(),
                new Quantity(entity.amount(), entity.unit())
        );
    }

    /**
     * Create {@link RecipeEntity} from {@link Recipe}
     * @param recipe the recipe
     * @return the recipe entity
     */
    public RecipeEntity recipeToEntity(Recipe recipe) { // <3>
        return RecipeEntity.of(
                recipe.getName(),
                recipe.getContent(),
                recipe.getIngredients().stream().map(this::ingredientToEntity).collect(Collectors.toSet())
        );
    }

    /**
     * Create {@link Recipe} from {@link RecipeEntity}
     * @param recipeEntity the recipe entity
     * @return the recipe
     */
    public Recipe recipeFromEntity(RecipeEntity recipeEntity) { // <4>
        return new Recipe(
                recipeEntity.name(),
                recipeEntity.content(),
                recipeEntity.ingredients().stream().map(this::ingredientFromEntity).toList()
        );
    }

}
