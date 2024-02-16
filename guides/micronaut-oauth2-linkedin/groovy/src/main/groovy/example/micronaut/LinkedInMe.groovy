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

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.NonNull
import io.micronaut.serde.annotation.Serdeable

import jakarta.validation.constraints.NotBlank

@CompileStatic
@Serdeable
class LinkedInMe {

    @NonNull
    @NotBlank
    final String id

    @NonNull
    @NotBlank
    final String localizedFirstName

    @NonNull
    @NotBlank
    final String localizedLastName

    LinkedInMe(@NonNull String id,
               @NonNull String localizedFirstName,
               @NonNull String localizedLastName) {
        this.id = id
        this.localizedFirstName = localizedFirstName
        this.localizedLastName = localizedLastName
    }
}
