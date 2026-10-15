/*
 * Copyright 2017-2026 original authors
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

import jakarta.inject.Singleton
import java.util.Optional

@Singleton // <1>
class DefaultFruitService(private val fruitRepository: FruitRepository) : FruitService {

    override fun list(): Iterable<Fruit> {
        return fruitRepository.findAll()
    }

    override fun save(fruit: Fruit): Fruit {
        return if (fruit.id == null) {
            fruitRepository.save(fruit)
        } else {
            fruitRepository.update(fruit)
        }
    }

    override fun find(id: String): Optional<Fruit> {
        return fruitRepository.findById(id)
    }

    override fun findByNameInList(name: List<String>): Iterable<Fruit> {
        return fruitRepository.findByNameInList(name)
    }
}
