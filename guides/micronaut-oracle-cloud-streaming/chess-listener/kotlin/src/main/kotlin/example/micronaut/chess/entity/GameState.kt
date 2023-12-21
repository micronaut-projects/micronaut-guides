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
package example.micronaut.chess.entity

import example.micronaut.chess.dto.GameStateDTO
import io.micronaut.data.annotation.DateCreated
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.Relation
import io.micronaut.data.annotation.Relation.Kind.MANY_TO_ONE
import java.time.LocalDateTime
import java.util.UUID
import jakarta.validation.constraints.Size

@MappedEntity("GAME_STATE")
class GameState(

    @field:Id
    val id: UUID,

    @field:Relation(MANY_TO_ONE)
    val game: Game,

    @field:Size(max = 1)
    val player: String,

    @field:Size(max = 10)
    val move: String,

    @field:Size(max = 100)
    val fen: String,

    val pgn: String) {

    @DateCreated
    var dateCreated: LocalDateTime? = null

    fun toDto() = GameStateDTO(id.toString(), game.id.toString(), player, move, fen, pgn)
}
