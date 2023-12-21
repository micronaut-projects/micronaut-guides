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

import io.micronaut.core.annotation.Creator
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import io.micronaut.serde.annotation.Serdeable;

import jakarta.validation.constraints.NotBlank

@Serdeable // <1>
class FruitCommand {

    @NonNull
    @NotBlank // <2>
    final String name

    @Nullable // <3>
    final String description

    FruitCommand(@NonNull String name) {
        this(name, null)
    }

    @Creator
    FruitCommand(@NonNull String name, @Nullable String description) {
        this.name = name
        this.description = description
    }
}
