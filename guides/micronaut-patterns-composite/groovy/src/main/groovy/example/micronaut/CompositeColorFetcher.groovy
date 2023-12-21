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
package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.context.annotation.Primary
import io.micronaut.http.HttpRequest
import jakarta.inject.Singleton

@CompileStatic
@Primary // <1>
@Singleton // <2>
class CompositeColorFetcher implements ColorFetcher {

    private final List<ColorFetcher> colorFetcherList

    CompositeColorFetcher(List<ColorFetcher> colorFetcherList) { // <3>
        this.colorFetcherList = colorFetcherList
    }

    @Override
    Optional<String> favouriteColor(HttpRequest<?> request) {
        return Optional.ofNullable(colorFetcherList.collect { it.favouriteColor(request) }
                .findAll { it.isPresent() }
                .collect { it.get() }[0])
    }
}
