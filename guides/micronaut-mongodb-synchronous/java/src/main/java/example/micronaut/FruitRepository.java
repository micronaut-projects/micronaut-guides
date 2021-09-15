package example.micronaut;

import io.micronaut.core.annotation.NonNull;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface FruitRepository {
    @NonNull
    List<Fruit> list();

    void save(@NonNull @NotNull @Valid Fruit fruit); // <1>
}
