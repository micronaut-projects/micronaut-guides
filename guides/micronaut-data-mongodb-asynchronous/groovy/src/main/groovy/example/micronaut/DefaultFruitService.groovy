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

import io.micronaut.core.annotation.NonNull
import jakarta.inject.Singleton
import org.reactivestreams.Publisher

@Singleton // <1>
class DefaultFruitService implements FruitService {

    private final FruitRepository fruitRepository

    DefaultFruitService(FruitRepository fruitRepository) {
        this.fruitRepository = fruitRepository
    }

    Publisher<Fruit> list() {
        fruitRepository.findAll()
    }

    Publisher<Fruit> save(Fruit fruit) {
        if (fruit.getId() == null) {
            fruitRepository.save(fruit)
        } else {
            fruitRepository.update(fruit)
        }
    }

    Publisher<Fruit> find(@NonNull String id) {
        fruitRepository.findById(id)
    }

    Publisher<Fruit> findByNameInList(List<String> name) {
        fruitRepository.findByNameInList(name)
    }
}
