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

import io.micronaut.http.HttpRequest;
import jakarta.inject.Singleton;

import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

@Singleton // <1>
public class PathColorFetcher implements ColorFetcher {

    private static final String[] COLORS = {
            "Red",
            "Blue",
            "Green",
            "Orange",
            "White",
            "Black",
            "Yellow",
            "Purple",
            "Silver",
            "Brown",
            "Gray",
            "Pink",
            "Olive",
            "Maroon",
            "Violet",
            "Charcoal",
            "Magenta",
            "Bronze",
            "Cream",
            "Gold",
            "Tan",
            "Teal",
            "Mustard",
            "Navy Blue",
            "Coral",
            "Burgundy",
            "Lavender",
            "Mauve",
            "Peach",
            "Rust",
            "Indigo",
            "Ruby",
            "Clay",
            "Cyan",
            "Azure",
            "Beige",
            "Turquoise",
            "Amber",
            "Mint"
    };

    @Override
    public Optional<String> favouriteColor(HttpRequest<?> request) {
        return Stream.of(COLORS)
                .filter(c -> request.getPath().contains(c.toLowerCase(Locale.ROOT)))
                .map(String::toLowerCase)
                .findFirst();
    }

    @Override
    public int getOrder() { // <2>
        return 20;
    }
}
