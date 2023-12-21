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
package example.micronaut.models

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import io.micronaut.core.annotation.Creator
import io.micronaut.core.annotation.Nullable
import io.micronaut.serde.annotation.Serdeable

import jakarta.validation.constraints.NotBlank

@CompileStatic
@EqualsAndHashCode
@Serdeable // <1>
class User {

    @Nullable
    Integer id // <2>

    @NotBlank @JsonProperty("first_name")
    String firstName

    @NotBlank @JsonProperty("last_name")
    String lastName

    @NotBlank
    String username

    @Creator
    User(Integer id,
         @NotBlank @JsonProperty("first_name") String firstName,
         @NotBlank @JsonProperty("last_name") String lastName,
         @NotBlank String username
    ) {
        this.id = id
        this.firstName = firstName
        this.lastName = lastName
        this.username = username
    }
}
