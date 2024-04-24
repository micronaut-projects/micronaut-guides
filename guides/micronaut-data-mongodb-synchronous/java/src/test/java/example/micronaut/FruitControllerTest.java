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

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
class FruitControllerTest {

    @Inject
    FruitClient fruitClient;

    @Test
    void emptyDatabaseContainsNoFruit() {
        assertEquals(0, StreamSupport.stream(fruitClient.list().spliterator(), false).count());
    }

    @Test
    void testInteractionWithTheController() {
        HttpResponse<Fruit> response = fruitClient.save(new Fruit("banana", null));
        assertEquals(HttpStatus.CREATED, response.getStatus());
        Fruit banana = response.getBody().get();

        Iterable<Fruit> fruits = fruitClient.list();

        List<Fruit> fruitList = StreamSupport.stream(fruits.spliterator(), false).collect(Collectors.toList());
        assertEquals(1, fruitList.size());
        assertEquals(banana.getName(), fruitList.get(0).getName());
        assertNull(fruitList.get(0).getDescription());

        response = fruitClient.save(new Fruit("apple", "Keeps the doctor away"));
        assertEquals(HttpStatus.CREATED, response.getStatus());

        fruits = fruitClient.list();
        assertTrue(StreamSupport.stream(fruits.spliterator(), false)
                .anyMatch(f -> "Keeps the doctor away".equals(f.getDescription())));

        banana.setDescription("Yellow and curved");
        fruitClient.update(banana);

        fruits = fruitClient.list();

        assertEquals(
                Stream.of("Keeps the doctor away", "Yellow and curved").collect(Collectors.toSet()),
                StreamSupport.stream(fruits.spliterator(), false)
                        .map(Fruit::getDescription)
                        .collect(Collectors.toSet())
        );
    }

    @Test
    void testSearchWorksAsExpected() {
        fruitClient.save(new Fruit("apple", "Keeps the doctor away"));
        fruitClient.save(new Fruit("pineapple", "Delicious"));
        fruitClient.save(new Fruit("lemon", "Lemonentary my dear Dr Watson"));

        Iterable<Fruit> fruit = fruitClient.query(Arrays.asList("apple", "pineapple"));

        assertTrue(StreamSupport.stream(fruit.spliterator(), false)
                .allMatch(f -> f.getName().equals("apple") || f.getName().equals("pineapple")));
    }
}
