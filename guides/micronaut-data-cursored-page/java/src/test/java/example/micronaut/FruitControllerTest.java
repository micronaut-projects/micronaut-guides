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

import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(transactional = false) // <1>
class FruitControllerTest {

    @Test
    void itIsPossibleToNavigateWithCursoredPage(@Client("/") HttpClient httpClient, // <2>
                                                FruitRepository repository) {
        List<Fruit> data = List.of(
                new Fruit(null, "apple"),
                new Fruit(null, "banana"),
                new Fruit(null, "cherry"),
                new Fruit(null, "date"),
                new Fruit(null, "elderberry"),
                new Fruit(null, "fig"),
                new Fruit(null, "grape"),
                new Fruit(null, "honeydew"),
                new Fruit(null, "kiwi"),
                new Fruit(null, "lemon")
        );
        repository.saveAll(data);
        int numberOfFruits = data.size();
        assertEquals(numberOfFruits, repository.count());

        BlockingHttpClient client = httpClient.toBlocking();
        HttpResponse<List<Fruit>> response = assertDoesNotThrow(() ->
                client.exchange(HttpRequest.GET("/fruits"), (Argument.listOf(Fruit.class))));
        assertEquals(HttpStatus.OK, response.getStatus());
        List<Fruit> fruits = response.body();
        assertEquals(numberOfFruits, fruits.size());
        repository.deleteAll();
    }
}
