package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Iterator;

public interface FruitRepository {
    @NonNull
    Iterable<Fruit> list();

    void save(@NonNull @NotNull @Valid Fruit fruit); // <1>
}
