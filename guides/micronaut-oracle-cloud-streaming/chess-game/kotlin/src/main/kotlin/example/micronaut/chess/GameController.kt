package example.micronaut.chess

import example.micronaut.chess.dto.GameDTO
import example.micronaut.chess.dto.GameStateDTO
import io.micronaut.http.MediaType
import io.micronaut.http.MediaType.APPLICATION_FORM_URLENCODED
import io.micronaut.http.MediaType.TEXT_PLAIN
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.reactivex.Single
import java.util.UUID

/**
 * Endpoints called by the front end using Ajax.
 */
@Controller("/game")
@ExecuteOn(TaskExecutors.IO) // <1>
class GameController(private val gameReporter: GameReporter) {

    /**
     * Start a new game.
     *
     * @param b black's name
     * @param w white's name
     * @return the id of the game
     */
    @Post(value = "/start", consumes = [APPLICATION_FORM_URLENCODED], produces = [TEXT_PLAIN]) // <2>
    fun start(b: String, w: String): Single<String> { // <3>
        val game = GameDTO(UUID.randomUUID().toString(), b, w) // <4>
        return gameReporter.game(game.id, game).map { game.id } // <5>
    }

    /**
     * Record a move.
     *
     * @param gameId the `Game` id
     * @param player b or w
     * @param move the current move
     * @param fen the current FEN string
     * @param pgn the current PGN string
     */
    @Post(value = "/move/{gameId}", consumes = [APPLICATION_FORM_URLENCODED]) // <6>
    fun move(@PathVariable gameId: String, player: String, move: String, fen: String, pgn: String) {
        val gameState = GameStateDTO(UUID.randomUUID().toString(),
                gameId, player, move, fen, pgn)
        gameReporter.gameState(gameId, gameState).subscribe() // <7>
    }

    /**
     * Record a checkmate.
     *
     * @param gameId the `Game` id
     * @param player b or w
     */
    @Post("/checkmate/{gameId}/{player}") // <8>
    fun checkmate(@PathVariable gameId: String, @PathVariable player: String) {
        val game = GameDTO(gameId, false, player)
        gameReporter.game(gameId, game).subscribe() // <9>
    }

    /**
     * Record a draw.
     *
     * @param gameId the `Game` id
     */
    @Post("/draw/{gameId}") // <10>
    fun draw(@PathVariable gameId: String) {
        val game = GameDTO(gameId, true, null)
        gameReporter.game(gameId, game).subscribe() // <11>
    }
}
