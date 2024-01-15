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

import io.micronaut.context.annotation.Factory
import io.micronaut.runtime.http.scope.RequestScope
import org.dataloader.DataLoader
import org.dataloader.DataLoaderRegistry
import org.slf4j.LoggerFactory

@Factory // <1>
class DataLoaderRegistryFactory {

    companion object {
        private val LOG = LoggerFactory.getLogger(DataLoaderRegistryFactory::class.java)
    }

    @Suppress("unused")
    @RequestScope // <2>
    fun dataLoaderRegistry(authorDataLoader: AuthorDataLoader): DataLoaderRegistry {
        val dataLoaderRegistry = DataLoaderRegistry()
        dataLoaderRegistry.register(
            "author",
            DataLoader.newMappedDataLoader(authorDataLoader)
        ) // <3>

        LOG.trace("Created new data loader registry")

        return dataLoaderRegistry
    }

}