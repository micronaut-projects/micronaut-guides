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

import io.micronaut.scheduling.TaskExecutors;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.dataloader.MappedBatchLoader;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@Singleton // <1>
public class AuthorDataLoader implements MappedBatchLoader<Long, Author> {

    private final AuthorRepository authorRepository;
    private final ExecutorService executor;

    public AuthorDataLoader(
            AuthorRepository authorRepository,
            @Named(TaskExecutors.BLOCKING) ExecutorService executor // <2>
    ) {
        this.authorRepository = authorRepository;
        this.executor = executor;
    }

    @Override
    public CompletionStage<Map<Long, Author>> load(Set<Long> keys) {
        return CompletableFuture.supplyAsync(() -> authorRepository
                        .findByIdIn(keys)
                        .stream()
                        .collect(toMap(Author::getId, Function.identity())),
                executor
        );
    }

}
