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

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import jakarta.inject.Singleton;

@Singleton // <1>
public class CompleteToDoDataFetcher implements DataFetcher<Boolean> {

    private final ToDoRepository toDoRepository;

    public CompleteToDoDataFetcher(ToDoRepository toDoRepository) { // <2>
        this.toDoRepository = toDoRepository;
    }

    @Override
    public Boolean get(DataFetchingEnvironment env) {
        long id = Long.parseLong(env.getArgument("id"));
        return toDoRepository
                .findById(id) // <3>
                .map(this::setCompletedAndUpdate)
                .orElse(false);
    }

    private boolean setCompletedAndUpdate(ToDo todo) {
        todo.setCompleted(true); // <4>
        toDoRepository.update(todo); // <5>
        return true;
    }
}
