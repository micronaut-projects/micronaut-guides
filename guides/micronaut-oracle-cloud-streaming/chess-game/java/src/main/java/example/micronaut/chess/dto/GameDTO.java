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
package example.micronaut.chess.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@Introspected // <1>
@JsonTypeInfo(use = NAME, property = "_className") // <2>
public class GameDTO {

    @Size(max = 36)
    @NotNull
    private final String id;

    @Size(max = 255)
    @Nullable
    private final String blackName;

    @Size(max = 255)
    @Nullable
    private final String whiteName;

    private final boolean draw;

    @Size(max = 1)
    private final Player winner;

    @Creator // <3>
    public GameDTO(@NonNull String id,
                   @Nullable String blackName,
                   @Nullable String whiteName,
                   boolean draw,
                   @Nullable Player winner) {
        this.id = id;
        this.blackName = blackName;
        this.whiteName = whiteName;
        this.draw = draw;
        this.winner = winner;
    }

    public GameDTO(@NonNull String id,
                   @NonNull String blackName,
                   @NonNull String whiteName) {
        this(id, blackName, whiteName, false, null);
    }

    public GameDTO(@NonNull String id,
                   boolean draw,
                   @Nullable Player winner) {
        this(id, null, null, draw, winner);
    }

    @NonNull
    public String getId() {
        return id;
    }

    @Nullable
    public String getBlackName() {
        return blackName;
    }

    @Nullable
    public String getWhiteName() {
        return whiteName;
    }

    public boolean isDraw() {
        return draw;
    }

    @Nullable
    public Player getWinner() {
        return winner;
    }
}
