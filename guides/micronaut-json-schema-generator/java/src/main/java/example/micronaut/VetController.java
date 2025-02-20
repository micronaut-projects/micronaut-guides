/*
 * Copyright 2017-2025 original authors
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

import com.example.animal.Animal;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.micronaut.http.annotation.Post;

@Controller("/test")
public class VetController {

    @Post("/type")
    @SingleResult
    AnimalTypes getAnimalType(@Body String animalJson) throws JsonProcessingException {
        JsonMapper jsonMapper = new JsonMapper();
        var animal = jsonMapper.readValue(animalJson, Animal.class);
        return AnimalTypes.of(animal.getClass().getName().substring(animal.getClass().getName().lastIndexOf('.') + 1));
    }
}
