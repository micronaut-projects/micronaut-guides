package example.micronaut.controller;

import example.micronaut.entity.IngredientEntity;
import example.micronaut.entity.RecipeEntity;
import example.micronaut.model.MeasurementUnit;
import example.micronaut.repository.RecipeRepository;
import io.micronaut.core.type.Argument;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import example.micronaut.model.Recipe;
import example.micronaut.model.Ingredient;
import example.micronaut.model.Quantity;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest
public class RecipeControllerTest {

    BlockingHttpClient client;
    RecipeRepository repository;

    RecipeControllerTest(
            @Client("/") HttpClient asyncClient,
            RecipeRepository repository
    ) {
        this.client = asyncClient.toBlocking();
        this.repository = repository;
    }

    @BeforeEach
    void setup() { // <1>
        if (!repository.findName(Pageable.unpaged()).isEmpty()) {
            return;
        }
        repository.save(RecipeEntity.of(
            "Macaroni",
            "Boil the macaroni in salted water until ready, then drain and transfer to a plate",
            Set.of(
                IngredientEntity.of("Macaroni", 200, MeasurementUnit.GRAM),
                IngredientEntity.of("Salt", 2, MeasurementUnit.GRAM)
            )
        ));
        repository.save(RecipeEntity.of(
            "Mac & Cheese",
            "Boil the macaroni, drain. Add evaporated milk and cheese to the pot, cook stirring till uniform",
            Set.of(
                IngredientEntity.of("Macaroni", 200, MeasurementUnit.GRAM),
                IngredientEntity.of("Evaporated milk", 0.2f, MeasurementUnit.LITER),
                IngredientEntity.of("Cheese", 150, MeasurementUnit.GRAM)
            )
        ));
        repository.save(RecipeEntity.of(
            "Grilled Cheese",
            "Make a sandwich with cheese. Grill it on a pan until toast is brown and cheese melted",
            Set.of(
                IngredientEntity.of("Toast bread slice", 2, MeasurementUnit.COUNT),
                IngredientEntity.of("Cheese", 50, MeasurementUnit.GRAM)
            )
        ));
    }

    @Test
    void searchRecipes() { // <2>
        Argument<List<String>> arg = Argument.listOf(String.class);
        List<String> recipes =
                client.retrieve(HttpRequest.GET("/searchRecipes?mealName=Mac"), arg);
        assertEquals(List.of("Macaroni", "Mac & Cheese"), recipes);

        recipes = client.retrieve(HttpRequest.GET("/searchRecipes?ingredientName=Cheese"), arg);
        assertEquals(List.of("Mac & Cheese", "Grilled Cheese"), recipes);
    }

    @Test
    void getRecipe() { // <3>
        String mealName = "Macaroni";
        HttpResponse<Recipe> response = client.exchange("/recipe/" + mealName, Recipe.class);
        Recipe recipe = response.body();
        assertEquals(mealName, recipe.getName());
        assertNotNull(recipe.getContent());
        Set<Ingredient> expected = Set.of(
                new Ingredient("Macaroni", new Quantity(200f, MeasurementUnit.GRAM)),
                new Ingredient("Salt", new Quantity(2f, MeasurementUnit.GRAM))
        );
        assertEquals(expected, Set.copyOf(recipe.getIngredients()));
        assertNotNull(response.header("Date"));
        assertNotNull(response.header("Last-Modified"));
    }

    @Test
    void searchRecipesPaginated() { // <4>
        Argument<List<String>> arg = Argument.listOf(String.class);
        HttpResponse<List<String>> recipes =
                client.exchange(HttpRequest.GET("/searchRecipes?size=2"), arg);
        assertEquals(List.of("Macaroni", "Mac & Cheese"), recipes.body());
        assertEquals("0", recipes.header("X-Page-Number"));
        assertEquals("2", recipes.header("X-Page-Count"));
        assertEquals("3", recipes.header("X-Total-Count"));

        recipes = client.exchange(HttpRequest.GET("/searchRecipes?page=1&size=2"), arg);
        assertEquals(List.of("Grilled Cheese"), recipes.body());
        assertEquals("1", recipes.header("X-Page-Number"));
        assertEquals("2", recipes.header("X-Page-Count"));
        assertEquals("3", recipes.header("X-Total-Count"));
        assertEquals("2", recipes.header("X-Page-Size"));
    }

}
