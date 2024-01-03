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
package example.micronaut

import io.micronaut.scheduling.TaskExecutors
import jakarta.inject.Named
import jakarta.inject.Singleton
import org.dataloader.MappedBatchLoader
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import java.util.concurrent.ExecutorService

@Singleton // <1>
class AuthorDataLoader(
    private val authorRepository: AuthorRepository,
    @Named(TaskExecutors.BLOCKING) val executor: ExecutorService // <2>
) : MappedBatchLoader<Long, Author> {

    override fun load(keys: MutableSet<Long>): CompletionStage<Map<Long, Author>> =
        CompletableFuture.supplyAsync({
            authorRepository
                .findByIdIn(keys.toList())
                .associateBy { it.id!! }
        }, executor)
}