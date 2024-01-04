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
import io.micronaut.core.annotation.Introspected
import jakarta.validation.constraints.Size

@Introspected // <1>
@JsonTypeInfo(use = NAME, property = "_className") // <2>
data class GameStateDTO(@field:Size(max = 36) val id: String,
                        @field:Size(max = 36) val gameId: String,
                        @field:Size(max = 1) val player: String,
                        @field:Size(max = 100) val move: String,
                        val fen: String,
                        @field:Size(max = 10) val pgn: String)
