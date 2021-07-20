package example.micronaut.chess;

import example.micronaut.chess.dto.GameDTO;
import example.micronaut.chess.dto.GameStateDTO;
import example.micronaut.chess.entity.Game;
import example.micronaut.chess.entity.GameState;
import example.micronaut.chess.repository.GameRepository;
import example.micronaut.chess.repository.GameStateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.transaction.Transactional;

/**
 * Service to encapsulate business logic.
 */
@Singleton
@Transactional
class GameService {

    private final Logger log = LoggerFactory.getLogger(GameService.class.getName());

    private final GameRepository gameRepository;
    private final GameStateRepository gameStateRepository;

    GameService(GameRepository gameRepository,
                GameStateRepository gameStateRepository) {
        this.gameRepository = gameRepository;
        this.gameStateRepository = gameStateRepository;
    }

    /**
     * Create a new <code>Game</code> and persist it.
     *
     * @param gameDTO the <code>Game</code> data
     * @return the game
     */
    public Game newGame(GameDTO gameDTO) {
        log.debug("New game {}, black: {}, white: {}",
                gameDTO.getId(), gameDTO.getBlackName(), gameDTO.getWhiteName());
        Game game = new Game(gameDTO.getId(),
                gameDTO.getBlackName(), gameDTO.getWhiteName());
        return gameRepository.save(game);
    }

    /**
     * Persist a game move as a <code>GameState</code>.
     *
     * @param gameStateDTO the <code>GameState</code> data
     */
    public void newGameState(GameStateDTO gameStateDTO) {
        Game game = findGame(gameStateDTO.getGameId());
        GameState gameState = new GameState(game,
                gameStateDTO.getPlayer(), gameStateDTO.getMove(),
                gameStateDTO.getFen(), gameStateDTO.getPgn());
        gameStateRepository.save(gameState);
    }

    /**
     * Record that a game ended in checkmate.
     *
     * @param gameDTO the <code>Game</code> data
     */
    public void checkmate(GameDTO gameDTO) {
        log.debug("Game {} ended with winner: {}",
                gameDTO.getId(), gameDTO.getWinner());
        Game game = findGame(gameDTO.getId());
        game.setWinner(gameDTO.getWinner());
        gameRepository.update(game);
    }

    /**
     * Record that a game ended in a draw.
     *
     * @param gameDTO the <code>Game</code> data
     */
    public void draw(GameDTO gameDTO) {
        log.debug("Game {} ended in a draw", gameDTO.getId());
        Game game = findGame(gameDTO.getId());
        game.setDraw(true);
        gameRepository.update(game);
    }

    private Game findGame(String gameId) {
        return gameRepository.findById(gameId).orElseThrow(() ->
                new IllegalArgumentException("Game with id '" + gameId + "' not found"));
    }
}
