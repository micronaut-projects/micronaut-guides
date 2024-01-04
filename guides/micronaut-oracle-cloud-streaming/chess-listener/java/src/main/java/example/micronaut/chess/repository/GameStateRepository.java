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
package example.micronaut.chess.repository;

import example.micronaut.chess.entity.GameState;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.Join;
import io.micronaut.data.repository.CrudRepository;

import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

import static io.micronaut.data.annotation.Join.Type.FETCH;

public interface GameStateRepository extends CrudRepository<GameState, UUID> {

    @Override
    @NonNull
    @Join(value = "game", type = FETCH) // <1>
    Optional<GameState> findById(@NotNull @NonNull UUID id);
}
