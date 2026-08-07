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
package example.micronaut

import io.micronaut.core.annotation.Nullable
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.Relation

@MappedEntity("contact") // <1>
data class ContactEntity(
    @field:Id // <2>
    @field:GeneratedValue // <3>
    @field:Nullable // <4>
    val id: Long? = null,

    val firstName: String?,

    val lastName: String?,

    @field:Relation(value = Relation.Kind.ONE_TO_MANY, mappedBy = "contact") // <5>
    val phones: List<PhoneEntity>?
)
