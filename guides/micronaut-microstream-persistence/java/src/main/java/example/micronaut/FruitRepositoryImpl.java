/*
 * Copyright 2017-2023 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.microstream.RootProvider;
import io.micronaut.microstream.annotations.StoreParams;
import io.micronaut.microstream.annotations.StoreReturn;
import jakarta.inject.Singleton;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.Map;

@Singleton // <1>
public class FruitRepositoryImpl implements FruitRepository {

    private final RootProvider<FruitContainer> rootProvider;

    FruitRepositoryImpl(RootProvider<FruitContainer> rootProvider) { // <2>
        this.rootProvider = rootProvider;
    }

    @Override
    @NonNull
    public Collection<Fruit> list() {
        return rootProvider.root().getFruits().values(); // <3>
    }

    @Override
    @NonNull
    public Fruit create(@NonNull @NotNull @Valid FruitCommand fruit) throws FruitDuplicateException {
        Map<String, Fruit> fruits = rootProvider.root().getFruits();
        if (fruits.containsKey(fruit.getName())) {
            throw new FruitDuplicateException(fruit.getName());
        }
        return performCreate(fruits, fruit);
    }

    @StoreParams("fruits") // <4>
    protected Fruit performCreate(Map<String, Fruit> fruits, FruitCommand fruit) {
        Fruit newFruit = new Fruit(fruit.getName(), fruit.getDescription());
        fruits.put(fruit.getName(), newFruit);
        return newFruit;
    }

    @Nullable
    public Fruit update(@NonNull @NotNull @Valid FruitCommand fruit) {
        Map<String, Fruit> fruits = rootProvider.root().getFruits();
        Fruit foundFruit = fruits.get(fruit.getName());
        if (foundFruit != null) {
            return performUpdate(foundFruit, fruit);
        }
        return null;
    }

    @StoreReturn // <5>
    protected Fruit performUpdate(@NonNull Fruit foundFruit, @NonNull FruitCommand fruit) {
        foundFruit.setDescription(fruit.getDescription());
        return foundFruit;
    }

    @Override
    @Nullable
    public Fruit find(@NonNull @NotBlank String name) {
        return rootProvider.root().getFruits().get(name);
    }

    @Override
    public void delete(@NonNull @NotNull @Valid FruitCommand fruit) {
        performDelete(fruit);
    }

    @StoreReturn // <5>
    protected Map<String, Fruit> performDelete(FruitCommand fruit) {
        if (rootProvider.root().getFruits().remove(fruit.getName()) != null) {
            return rootProvider.root().getFruits();
        }
        return null;
    }
}
