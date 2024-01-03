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

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import jakarta.inject.Singleton;

@Singleton // <1>
public class ToDosDataFetcher implements DataFetcher<Iterable<ToDo>> {

    private final ToDoRepository toDoRepository;

    public ToDosDataFetcher(ToDoRepository toDoRepository) { // <2>
        this.toDoRepository = toDoRepository;
    }

    @Override
    public Iterable<ToDo> get(DataFetchingEnvironment env) {
        return toDoRepository.findAll();
    }
}
