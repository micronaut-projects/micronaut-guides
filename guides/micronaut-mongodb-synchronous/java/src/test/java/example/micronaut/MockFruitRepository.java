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
package example.micronaut;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;

import java.util.Collections;
import java.util.List;

@Requires(property = "spec.name", value = "FruitControllerValidationTest")
@Singleton
@Replaces(FruitRepository.class)
class MockFruitRepository implements FruitRepository {

    @Override
    public List<Fruit> list() {
        return Collections.emptyList();
    }

    @Override
    public void save(Fruit fruit) {
    }
}
