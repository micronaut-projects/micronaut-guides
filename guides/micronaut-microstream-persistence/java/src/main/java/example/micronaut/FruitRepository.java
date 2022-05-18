package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import javax.validation.Valid;

interface FruitRepository {

    @NonNull
    Collection<Fruit> list();

    @NonNull
    Fruit create(@NonNull @NotNull @Valid FruitCommand fruit) // <1>
            throws FruitDuplicateException;

    @Nullable
    Fruit update(@NonNull @NotNull @Valid FruitCommand fruit); // <1>

    @Nullable
    Fruit find(@NonNull @NotBlank String name);

    void delete(@NonNull @NotNull @Valid FruitCommand fruit); // <1>
}
