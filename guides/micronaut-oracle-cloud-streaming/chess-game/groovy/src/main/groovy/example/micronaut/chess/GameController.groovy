package example.micronaut.chess

import example.micronaut.chess.dto.GameDTO
import example.micronaut.chess.dto.GameStateDTO
import groovy.transform.CompileStatic
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Status
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import reactor.core.publisher.Mono

import static io.micronaut.http.HttpStatus.CREATED
import static io.micronaut.http.HttpStatus.NO_CONTENT
import static io.micronaut.http.MediaType.APPLICATION_FORM_URLENCODED
import static io.micronaut.http.MediaType.TEXT_PLAIN

/**
 * Endpoints called by the front end using Ajax.
 */
@Controller('/game') // <1>
@ExecuteOn(TaskExecutors.IO) // <2>
@CompileStatic
class GameController {

    private final GameReporter gameReporter

    GameController(GameReporter gameReporter) {
        this.gameReporter = gameReporter
    }

    /**
     * Start a new game.
     *
     * @param b black's name
     * @param w white's name
     * @return the id of the game
     */
    @Post(value = '/start', // <3>
          consumes = APPLICATION_FORM_URLENCODED, // <4>
          produces = TEXT_PLAIN) // <5>
    @Status(CREATED) // <6>
    Mono<String> start(String b, // <7>
                       String w) {
        GameDTO game = new GameDTO(UUID.randomUUID().toString(), b, w) // <8>
        gameReporter.game(game.id, game).map(gameDTO -> game.id) // <9>
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
    @Post(value = '/move/{gameId}', // <10>
          consumes = APPLICATION_FORM_URLENCODED) // <11>
    @Status(CREATED) // <12>
    void move(@PathVariable String gameId,
              String player,
              String move,
              String fen,
              String pgn) {
        GameStateDTO gameState = new GameStateDTO(UUID.randomUUID().toString(),
                gameId, player, move, fen, pgn)
        gameReporter.gameState(gameId, gameState).subscribe() // <13>
    }

    /**
     * Record a checkmate.
     *
     * @param gameId the <code>Game</code> id
     * @param player b or w
     */
    @Post('/checkmate/{gameId}/{player}') // <14>
    @Status(NO_CONTENT) // <15>
    void checkmate(@PathVariable String gameId,
                   @PathVariable String player) {
        GameDTO game = new GameDTO(gameId, false, player)
        gameReporter.game(gameId, game).subscribe() // <16>
    }

    /**
     * Record a draw.
     *
     * @param gameId the <code>Game</code> id
     */
    @Post('/draw/{gameId}') // <17>
    @Status(NO_CONTENT) // <18>
    void draw(@PathVariable String gameId) {
        GameDTO game = new GameDTO(gameId, true, null)
        gameReporter.game(gameId, game).subscribe() // <19>
    }
}
