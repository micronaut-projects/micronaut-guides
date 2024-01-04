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
package example.micronaut.domain

import example.micronaut.UserState
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.serde.annotation.Serdeable
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.validation.constraints.NotBlank

@Serdeable  // <1>
@MappedEntity // <2>
class User implements UserState {
    @Id // <3>
    @GeneratedValue // <4>
    Long id

    @NotBlank
    String email

    @NotBlank
    String username

    @NotBlank
    String password

    boolean enabled
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired
}
