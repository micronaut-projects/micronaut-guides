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
package example.micronaut.chess.dto

import com.fasterxml.jackson.annotation.JsonTypeInfo
import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Introspected
import io.micronaut.core.annotation.NonNull

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME

@Introspected // <1>
@JsonTypeInfo(use = NAME, property = '_className') // <2>
@CompileStatic
class GameStateDTO {

    @Size(max = 36)
    @NotNull
    @NonNull
    final String id

    @Size(max = 36)
    @NotNull
    @NonNull
    final String gameId

    @Size(max = 1)
    @NotNull
    @NonNull
    final Player player

    @Size(max = 100)
    @NotNull
    @NonNull
    final String fen

    @NotNull
    @NonNull
    final String pgn

    @Size(max = 10)
    @NotNull
    @NonNull
    final String move

    GameStateDTO(@NonNull String id,
                 @NonNull String gameId,
                 @NonNull Player player,
                 @NonNull String move,
                 @NonNull String fen,
                 @NonNull String pgn) {
        this.id = id
        this.gameId = gameId
        this.player = player
        this.move = move
        this.fen = fen
        this.pgn = pgn
    }
}
