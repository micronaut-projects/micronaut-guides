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
package example.micronaut.chess;

import example.micronaut.chess.dto.GameDTO;
import example.micronaut.chess.dto.GameStateDTO;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;

import static io.micronaut.configuration.kafka.annotation.OffsetReset.EARLIEST;

@KafkaListener(offsetReset = EARLIEST) // <1>
class ChessListener {

    private final GameService gameService;

    ChessListener(GameService gameService) { // <2>
        this.gameService = gameService;
    }

    @Topic("chessGame") // <3>
    void onGame(GameDTO gameDTO) {
        if (gameDTO.isDraw()) {
            gameService.draw(gameDTO); // <4>
        }
        else if (gameDTO.getWinner() != null) {
            gameService.checkmate(gameDTO); // <5>
        }
        else {
            gameService.newGame(gameDTO); // <6>
        }
    }

    @Topic("chessGameState") // <3>
    void onGameState(GameStateDTO gameState) {
        gameService.newGameState(gameState); // <7>
    }
}
