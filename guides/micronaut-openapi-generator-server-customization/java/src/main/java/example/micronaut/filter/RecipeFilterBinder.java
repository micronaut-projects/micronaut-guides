package example.micronaut.filter;

import io.micronaut.core.convert.ArgumentConversionContext;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpParameters;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.bind.binders.TypedRequestArgumentBinder;
import io.micronaut.http.exceptions.HttpStatusException;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
public class RecipeFilterBinder implements TypedRequestArgumentBinder<RecipeFilter> {

    public static final String MEAL_NAME_QUERY_PARAMETER = "mealName";
    public static final String INGREDIENT_NAME_QUERY_PARAMETER = "ingredientName";

    @Override
    public Argument<RecipeFilter> argumentType() {
        return Argument.of(RecipeFilter.class);
    }

    @Override
    public BindingResult<RecipeFilter> bind(ArgumentConversionContext<RecipeFilter> context, HttpRequest<?> source) { // <1>
        HttpParameters parameters = source.getParameters();
        String mealName = parameters.get(MEAL_NAME_QUERY_PARAMETER);
        String ingredientName = parameters.get(INGREDIENT_NAME_QUERY_PARAMETER);
        if (mealName != null && ingredientName != null) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, "Only one of 'mealName' and 'ingredientName' filters is allowed");
        }
        return () -> Optional.of(new RecipeFilter(mealName, ingredientName));
    }
}
