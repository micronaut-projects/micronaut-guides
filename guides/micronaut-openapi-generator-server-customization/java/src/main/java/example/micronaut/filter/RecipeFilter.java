package example.micronaut.filter;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.annotation.QueryValue;

/**
 * A type for describing how recipe results should be filtered.
 *
 * @param mealName The name of the meal
 * @param ingredientName An ingredient name
 */
public record RecipeFilter (
        @Nullable
        String mealName, // <1>
        @Nullable
        String ingredientName // <2>
) {

}
