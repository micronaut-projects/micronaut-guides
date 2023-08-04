package example.micronaut.repository;

import example.micronaut.entity.RecipeEntity;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.Join;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.PageableRepository;

import java.util.List;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface RecipeRepository extends PageableRepository<RecipeEntity, Long> {

    @NonNull
    <S extends RecipeEntity> S save(S recipe); // <1>

    @NonNull
    Page<String> findNameByNameContains(String content, Pageable pageable); // <2>

    @NonNull
    @Join("ingredients")
    @Query(value = "SELECT r.name FROM ingredient i LEFT JOIN recipe r ON i.recipe_id = r.id WHERE i.name = :ingredientsName",
           countQuery = "SELECT count(r.id) FROM ingredient i LEFT JOIN recipe r ON i.recipe_id = r.id WHERE i.name = :ingredientsName")
    Page<String> findNameByIngredientsName(String ingredientsName, Pageable pageable); // <3>

    @NonNull
    Page<String> findName(Pageable pageable); // <4>

    @NonNull
    @Join("ingredients")
    RecipeEntity find(String name); // <5>

}
