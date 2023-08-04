package example.micronaut.controller;

import example.micronaut.dated.DatedResponse;
import example.micronaut.entity.RecipeEntity;
import example.micronaut.filter.RecipeFilter;
import example.micronaut.entity.EntityMapper;
import example.micronaut.repository.RecipeRepository;
import example.micronaut.model.Recipe;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import example.micronaut.api.RecipeApi;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import java.util.List;

@Controller
@ExecuteOn(TaskExecutors.IO)
public class RecipeController implements RecipeApi { // <1>

    private final RecipeRepository repository;

    private final EntityMapper mapper;

    public RecipeController(RecipeRepository repository, EntityMapper mapper) { // <2>
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void addRecipe(Recipe recipe) { // <3>
        repository.save(mapper.recipeToEntity(recipe));
    }

    @Override
    public DatedResponse<Recipe> getRecipe(String name) { // <4>
        RecipeEntity recipe = repository.find(name);
        return DatedResponse.of(mapper.recipeFromEntity(recipe))
                .withLastModified(recipe.dateUpdated())
                .withCreated(recipe.dateCreated());
    }

    @Override
    public Page<String> search(RecipeFilter filter, Pageable pageable) { // <5>
        if (filter.ingredientName() != null) {
            return repository.findNameByIngredientsName(filter.ingredientName(), pageable);
        } else if (filter.mealName() != null) {
            return repository.findNameByNameContains(filter.mealName(), pageable);
        } else {
            return repository.findName(pageable);
        }
    }

}
