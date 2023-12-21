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

import example.micronaut.chess.dto.Player
import example.micronaut.chess.dto.GameDTO
import groovy.transform.CompileStatic
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import jakarta.validation.constraints.NotBlank
import io.micronaut.data.annotation.DateCreated
import io.micronaut.data.annotation.DateUpdated
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

@MappedEntity('GAME')
@CompileStatic
class Game {

    @Id
    @NotNull
    final UUID id

    @Size(max = 255)
    @NotBlank
    @NonNull
    final String blackName

    @Size(max = 255)
    @NotBlank
    @NonNull
    final String whiteName

    @DateCreated
    LocalDateTime dateCreated

    @DateUpdated
    LocalDateTime dateUpdated

    boolean draw

    @Nullable
    @Size(max = 1)
    Player winner

    Game(@NonNull UUID id,
         @NonNull String blackName,
         @NonNull String whiteName) {
        this.id = id
        this.blackName = blackName
        this.whiteName = whiteName
    }

    GameDTO toDto() {
        new GameDTO(id.toString(), blackName, whiteName, draw, winner)
    }
}
