/*
 * Copyright 2017-2026 original authors
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

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.Reservable
import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.PositiveOrZero

@Serdeable // <1>
@MappedEntity("ACCOUNT") // <2>
data class Account(
    @field:Id @field:GeneratedValue val id: Long? = null, // <3>
    @field:NotBlank val name: String, // <4>
    @field:Reservable @field:PositiveOrZero val balance: Long, // <5>
    @field:Reservable @field:PositiveOrZero val credit: Long // <6>
)
