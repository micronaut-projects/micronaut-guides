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
package example.micronaut.chess.dto

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME
import io.micronaut.core.annotation.Creator
import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.Size

@Serdeable // <1>
@JsonTypeInfo(use = NAME, property = "_className") // <2>
class GameDTO

    @Creator // <3>
    constructor(@field:Size(max = 36) val id: String,
                @field:Size(max = 255) val blackName: String?,
                @field:Size(max = 255) val whiteName: String?,
                val draw: Boolean,
                @field:Size(max = 1) val winner: String?) {

    constructor(id: String, blackName: String, whiteName: String) :
            this(id, blackName, whiteName, false, null)

    constructor(id: String, draw: Boolean, winner: String?) :
            this(id, null, null, draw, winner)
}
