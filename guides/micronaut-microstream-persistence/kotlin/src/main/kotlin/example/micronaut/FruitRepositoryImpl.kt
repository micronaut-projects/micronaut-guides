/*
 * Copyright 2017-2024 original authors
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
package example.micronaut

import io.micronaut.microstream.RootProvider
import io.micronaut.microstream.annotations.StoreParams
import io.micronaut.microstream.annotations.StoreReturn
import jakarta.inject.Inject
import jakarta.inject.Singleton
import jakarta.validation.Valid

@Singleton // <1>
open class FruitRepositoryImpl: FruitRepository {

    @Inject
    private lateinit var rootProvider: RootProvider<FruitContainer> // <2>

    override fun list() = rootProvider.root().fruits.values // <3>

    override fun create(@Valid fruit: FruitCommand): Fruit {
        val fruits: MutableMap<String, Fruit> = rootProvider.root().fruits

        if (fruits.containsKey(fruit.name)) {
            throw FruitDuplicateException(fruit.name)
        }
        return performCreate(fruits, fruit)
    }

    @StoreParams("fruits") // <4>
    protected open fun performCreate(fruits: MutableMap<String, Fruit>,
                                     fruit: FruitCommand): Fruit {
        val newFruit = Fruit(fruit.name, fruit.description)
        fruits[fruit.name] = newFruit
        return newFruit
    }

    override fun update(@Valid fruit: FruitCommand): Fruit? {
        val fruits: Map<String, Fruit> = rootProvider.root().fruits
        val foundFruit = fruits[fruit.name]
        return foundFruit?.let { performUpdate(it, fruit) }
    }

    @StoreReturn // <5>
    protected open fun performUpdate(foundFruit: Fruit,
                                     fruit: FruitCommand): Fruit {
        foundFruit.description = fruit.description
        return foundFruit
    }

    override fun find(name: String) = rootProvider.root().fruits[name]

    override fun delete(@Valid fruit: FruitCommand) {
        performDelete(fruit)
    }

    @StoreReturn // <5>
    protected open fun performDelete(fruit: FruitCommand): Map<String, Fruit>? {
        return if (rootProvider.root().fruits.remove(fruit.name) != null) {
            rootProvider.root().fruits
        } else null
    }
}