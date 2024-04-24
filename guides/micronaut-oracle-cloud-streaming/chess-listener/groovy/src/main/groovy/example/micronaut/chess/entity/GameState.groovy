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

import example.micronaut.chess.dto.GameStateDTO
import groovy.transform.CompileStatic
import io.micronaut.core.annotation.NonNull
import io.micronaut.data.annotation.DateCreated
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.Relation
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import example.micronaut.chess.dto.Player
import java.time.LocalDateTime

import static io.micronaut.data.annotation.Relation.Kind.MANY_TO_ONE

@MappedEntity('GAME_STATE')
@CompileStatic
class GameState {

    @Id
    @NotNull
    @NonNull
    final UUID id

    @Relation(MANY_TO_ONE)
    @NotNull
    @NonNull
    final Game game

    @DateCreated
    LocalDateTime dateCreated

    @Size(max = 1)
    @NotBlank
    @NonNull
    final Player player

    @Size(max = 100)
    @NotBlank
    @NonNull
    final String fen

    @NotBlank
    @NonNull
    final String pgn

    @Size(max = 10)
    @NotBlank
    @NonNull
    final String move

    /**
     * @param id the id
     * @param game the game
     * @param player b or w
     * @param move the current move
     * @param fen https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation
     * @param pgn https://en.wikipedia.org/wiki/Portable_Game_Notation
     */
    GameState(@NonNull UUID id,
              @NonNull Game game,
              @NonNull Player player,
              @NonNull String move,
              @NonNull String fen,
              @NonNull String pgn) {
        this.id = id;
        this.game = game
        this.player = player
        this.move = move
        this.fen = fen
        this.pgn = pgn
    }

    @NonNull
    GameStateDTO toDto() {
        new GameStateDTO(id.toString(), game.getId().toString(), player, move, fen, pgn)
    }
}
