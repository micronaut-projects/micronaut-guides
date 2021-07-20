package example.micronaut.chess;

import example.micronaut.chess.dto.GameDTO;
import example.micronaut.chess.dto.GameStateDTO;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.reactivex.Single;

import java.util.UUID;

import static io.micronaut.http.MediaType.APPLICATION_FORM_URLENCODED;
import static io.micronaut.http.MediaType.TEXT_PLAIN;

/**
 * Endpoints called by the front end using Ajax.
 */
@Controller("/game")
@ExecuteOn(TaskExecutors.IO) // <1>
class GameController {

    private final GameReporter gameReporter;

    GameController(GameReporter gameReporter) {
        this.gameReporter = gameReporter;
    }

    /**
     * Start a new game.
     *
     * @param b black's name
     * @param w white's name
     * @return the id of the game
     */
    @Post(value = "/start", consumes = APPLICATION_FORM_URLENCODED, produces = TEXT_PLAIN) // <2>
    Single<String> start(String b, String w) { // <3>
        GameDTO game = new GameDTO(UUID.randomUUID().toString(), b, w); // <4>
        return gameReporter.game(game.getId(), game).map(gameDTO -> game.getId()); // <5>
    }

    /**
     * Record a move.
     *
     * @param gameId the <code>Game</code> id
     * @param player b or w
     * @param move the current move
     * @param fen the current FEN string
     * @param pgn the current PGN string
     */
    @Post(value = "/move/{gameId}", consumes = APPLICATION_FORM_URLENCODED) // <6>
    void move(@PathVariable String gameId, String player, String move, String fen, String pgn) {
        GameStateDTO gameState = new GameStateDTO(UUID.randomUUID().toString(),
                gameId, player, move, fen, pgn);
        gameReporter.gameState(gameId, gameState).subscribe(); // <7>
    }

    /**
     * Record a checkmate.
     *
     * @param gameId the <code>Game</code> id
     * @param player b or w
     */
    @Post("/checkmate/{gameId}/{player}") // <8>
    void checkmate(@PathVariable String gameId, @PathVariable String player) {
        GameDTO game = new GameDTO(gameId, false, player);
        gameReporter.game(gameId, game).subscribe(); // <9>
    }

    /**
     * Record a draw.
     *
     * @param gameId the <code>Game</code> id
     */
    @Post("/draw/{gameId}") // <10>
    void draw(@PathVariable String gameId) {
        GameDTO game = new GameDTO(gameId, true, null);
        gameReporter.game(gameId, game).subscribe(); // <11>
    }
}
