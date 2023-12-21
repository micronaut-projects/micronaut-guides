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
package example.micronaut.chess

import example.micronaut.chess.dto.GameDTO
import example.micronaut.chess.dto.GameStateDTO
import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.OffsetReset.EARLIEST
import io.micronaut.configuration.kafka.annotation.Topic

@KafkaListener(offsetReset = EARLIEST) // <1>
class ChessListener(private val gameService: GameService) { // <2>

    @Topic("chessGame") // <3>
    fun onGame(gameDTO: GameDTO) {
        if (gameDTO.draw) {
            gameService.draw(gameDTO) // <4>
        } else if (gameDTO.winner != null) {
            gameService.checkmate(gameDTO) // <5>
        } else {
            gameService.newGame(gameDTO) // <6>
        }
    }

    @Topic("chessGameState") // <3>
    fun onGameState(gameState: GameStateDTO) = gameService.newGameState(gameState) // <7>
}
