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
package example.micronaut.chess.entity

import example.micronaut.chess.dto.GameDTO
import io.micronaut.core.annotation.Nullable
import io.micronaut.data.annotation.DateCreated
import io.micronaut.data.annotation.DateUpdated
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import java.time.LocalDateTime
import java.util.UUID
import jakarta.validation.constraints.Size

@MappedEntity("GAME")
class Game(

    @field:Id
    val id: UUID,

    @field:Size(max = 255)
    val blackName: String,

    @field:Size(max = 255)
    val whiteName: String) {

    @DateCreated
    var dateCreated: LocalDateTime? = null

    @DateUpdated
    var dateUpdated: LocalDateTime? = null

    var draw = false

    @Nullable
    @field:Size(max = 1) var winner: String? = null

    fun toDto() = GameDTO(id.toString(), blackName, whiteName, draw, winner)
}
