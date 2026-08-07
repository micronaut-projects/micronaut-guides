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

import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero

@Serdeable
data class SortingAndOrderArguments(
    @field:PositiveOrZero // <1>
    var offset: Int? = null,

    @field:Positive // <1>
    var max: Int? = null,

    @field:Pattern(regexp = "id|name") // <1>
    var sort: String? = null,

    @field:Pattern(regexp = "asc|ASC|desc|DESC") // <1>
    var order: String? = null
)
