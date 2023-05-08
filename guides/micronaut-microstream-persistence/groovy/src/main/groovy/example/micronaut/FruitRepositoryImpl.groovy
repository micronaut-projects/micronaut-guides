package example.micronaut

import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import io.micronaut.microstream.RootProvider
import io.micronaut.microstream.annotations.StoreParams
import io.micronaut.microstream.annotations.StoreReturn
import jakarta.inject.Singleton

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

@Singleton // <1>
class FruitRepositoryImpl implements FruitRepository {

    private final RootProvider<FruitContainer> rootProvider

    FruitRepositoryImpl(RootProvider<FruitContainer> rootProvider) { // <2>
        this.rootProvider = rootProvider
    }

    @Override
    @NonNull
    Collection<Fruit> list() {
        return rootProvider.root().fruits.values() // <3>
    }

    @Override
    @NonNull
    Fruit create(@NonNull @NotNull @Valid FruitCommand fruit) {
        Map<String, Fruit> fruits = rootProvider.root().fruits
        if (fruits.containsKey(fruit.name)) {
            throw new FruitDuplicateException(fruit.name)
        }
        return performCreate(fruits, fruit)
    }

    @StoreParams("fruits") // <4>
    protected Fruit performCreate(Map<String, Fruit> fruits, FruitCommand fruit) {
        Fruit newFruit = new Fruit(fruit.name, fruit.description)
        fruits.put(fruit.name, newFruit)
        return newFruit
    }

    @Nullable
    Fruit update(@NonNull @NotNull @Valid FruitCommand fruit) {
        Map<String, Fruit> fruits = rootProvider.root().getFruits()
        Fruit foundFruit = fruits.get(fruit.name)
        if (foundFruit != null) {
            return performUpdate(foundFruit, fruit)
        }
        return null
    }

    @StoreReturn // <5>
    protected Fruit performUpdate(@NonNull Fruit foundFruit, @NonNull FruitCommand fruit) {
        foundFruit.setDescription(fruit.description)
        return foundFruit
    }

    @Override
    @Nullable
    Fruit find(@NonNull @NotBlank String name) {
        return rootProvider.root().fruits.get(name)
    }

    @Override
    void delete(@NonNull @NotNull @Valid FruitCommand fruit) {
        performDelete(fruit)
    }

    @StoreReturn // <5>
    protected Map<String, Fruit> performDelete(FruitCommand fruit) {
        if (rootProvider.root().fruits.remove(fruit.name) != null) {
            return rootProvider.root().fruits
        }
        return null
    }
}
