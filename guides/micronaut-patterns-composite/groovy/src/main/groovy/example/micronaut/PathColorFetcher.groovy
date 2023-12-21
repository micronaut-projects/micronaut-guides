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
import io.micronaut.http.HttpRequest
import jakarta.inject.Singleton

@CompileStatic
@Singleton // <1>
class PathColorFetcher implements ColorFetcher {

    private static final String[] COLORS = [
            'Red',
            'Blue',
            'Green',
            'Orange',
            'White',
            'Black',
            'Yellow',
            'Purple',
            'Silver',
            'Brown',
            'Gray',
            'Pink',
            'Olive',
            'Maroon',
            'Violet',
            'Charcoal',
            'Magenta',
            'Bronze',
            'Cream',
            'Gold',
            'Tan',
            'Teal',
            'Mustard',
            'Navy Blue',
            'Coral',
            'Burgundy',
            'Lavender',
            'Mauve',
            'Peach',
            'Rust',
            'Indigo',
            'Ruby',
            'Clay',
            'Cyan',
            'Azure',
            'Beige',
            'Turquoise',
            'Amber',
            'Mint'
    ]

    @Override
    Optional<String> favouriteColor(HttpRequest<?> request) {
        return Optional.ofNullable(
                COLORS.findAll { request.path.contains(it.toLowerCase(Locale.ROOT)) }
                      .collect { it.toLowerCase() }[0]
        )
    }

    @Override
    int getOrder() { // <2>
        return 20
    }
}
