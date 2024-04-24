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
import jakarta.transaction.Transactional;

@Singleton // <1>
public class CreateToDoDataFetcher implements DataFetcher<ToDo> {

    private final ToDoRepository toDoRepository;
    private final AuthorRepository authorRepository;

    public CreateToDoDataFetcher(ToDoRepository toDoRepository, // <2>
                                 AuthorRepository authorRepository) {
        this.toDoRepository = toDoRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    @Transactional
    public ToDo get(DataFetchingEnvironment env) {
        String title = env.getArgument("title");
        String username = env.getArgument("author");

        Author author = authorRepository.findOrCreate(username); // <3>

        ToDo toDo = new ToDo(title, author.getId());

        return toDoRepository.save(toDo); // <4>
    }
}
