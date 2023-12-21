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
import example.micronaut.chess.entity.Game;
import example.micronaut.chess.entity.GameState;
import example.micronaut.chess.repository.GameRepository;
import example.micronaut.chess.repository.GameStateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import java.util.UUID;

@Singleton
@Transactional
public class GameService {

    private final Logger log = LoggerFactory.getLogger(GameService.class.getName());

    private final GameRepository gameRepository;
    private final GameStateRepository gameStateRepository;

    GameService(GameRepository gameRepository,
                GameStateRepository gameStateRepository) {
        this.gameRepository = gameRepository;
        this.gameStateRepository = gameStateRepository;
    }

    public Game newGame(GameDTO gameDTO) {
        log.debug("New game {}, black: {}, white: {}",
                gameDTO.getId(), gameDTO.getBlackName(), gameDTO.getWhiteName());
        Game game = new Game(UUID.fromString(gameDTO.getId()),
                gameDTO.getBlackName(), gameDTO.getWhiteName());
        return gameRepository.save(game);
    }

    public void newGameState(GameStateDTO gameStateDTO) {
        Game game = findGame(gameStateDTO.getGameId());
        GameState gameState = new GameState(
                UUID.fromString(gameStateDTO.getId()), game,
                gameStateDTO.getPlayer(), gameStateDTO.getMove(),
                gameStateDTO.getFen(), gameStateDTO.getPgn());
        gameStateRepository.save(gameState);
    }

    public void checkmate(GameDTO gameDTO) {
        log.debug("Game {} ended with winner: {}",
                gameDTO.getId(), gameDTO.getWinner());
        Game game = findGame(gameDTO.getId());
        game.setWinner(gameDTO.getWinner());
        gameRepository.update(game);
    }

    public void draw(GameDTO gameDTO) {
        log.debug("Game {} ended in a draw", gameDTO.getId());
        Game game = findGame(gameDTO.getId());
        game.setDraw(true);
        gameRepository.update(game);
    }

    @NonNull
    private Game findGame(String gameId) {
        return gameRepository.findById(UUID.fromString(gameId)).orElseThrow(() ->
                new IllegalArgumentException("Game with id '" + gameId + "' not found"));
    }
}
