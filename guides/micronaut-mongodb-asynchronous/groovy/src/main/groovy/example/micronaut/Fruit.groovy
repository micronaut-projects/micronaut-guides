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
import io.micronaut.core.annotation.Creator
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import io.micronaut.serde.annotation.Serdeable
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonProperty

import jakarta.validation.constraints.NotBlank

@CompileStatic
@Serdeable // <1>
class Fruit {

    @NonNull
    @NotBlank // <2>
    @BsonProperty('name') // <3>
    private final String name

    @Nullable
    @BsonProperty('description') // <3>
    private final String description

    Fruit(@NonNull String name) {
        this(name, null)
    }

    @Creator // <4>
    @BsonCreator// <3>
    Fruit(@NonNull @BsonProperty('name') String name,  // <3>
          @Nullable @BsonProperty('description') String description) {  // <3>
        this.name = name
        this.description = description
    }

    @NonNull
    String getName() {
        name
    }

    @Nullable
    String getDescription() {
        description
    }
}
