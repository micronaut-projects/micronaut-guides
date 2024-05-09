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

import io.micronaut.data.model.CursoredPage;
import io.micronaut.data.model.CursoredPageable;
import io.micronaut.data.model.Sort;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.util.ArrayList;
import java.util.List;

@Controller("/fruits") // <1>
class FruitController {
    private static final Sort SORT = Sort.of(Sort.Order.asc("name"));

    private final FruitRepository repository;

    FruitController(FruitRepository repository) { // <2>
        this.repository = repository;
    }

    @Get // <3>
    List<Fruit> index() {
        CursoredPage<Fruit> page = repository.find(CursoredPageable.from(2, SORT));  // <4>
        List<Fruit> fruits = new ArrayList<>(page.getContent());
        while (page.hasNext()) {
            page = repository.find(page.nextPageable());
            fruits.addAll(page.getContent());
        }
        return fruits;
    }
}