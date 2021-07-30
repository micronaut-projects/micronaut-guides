package example.micronaut.chess

import example.micronaut.chess.dto.GameDTO
import example.micronaut.chess.dto.GameStateDTO
import io.micronaut.http.HttpStatus.CREATED
import io.micronaut.http.HttpStatus.NO_CONTENT
import io.micronaut.http.MediaType.APPLICATION_FORM_URLENCODED
import io.micronaut.http.MediaType.TEXT_PLAIN
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Status
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import reactor.core.publisher.Mono
import java.util.UUID

@Controller("/game") // <1>
@ExecuteOn(TaskExecutors.IO) // <2>
class GameController(private val gameReporter: GameReporter) {

    @Post(value = "/start", // <3>
          consumes = [APPLICATION_FORM_URLENCODED], // <4>
          produces = [TEXT_PLAIN]) // <5>
    @Status(CREATED) // <6>
    fun start(b: String,
              w: String): Mono<String> { // <7>
        val game = GameDTO(UUID.randomUUID().toString(), b, w) // <8>
        return gameReporter.game(game.id, game).map { game.id } // <9>
    }

    @Post(value = "/move/{gameId}", // <10>
          consumes = [APPLICATION_FORM_URLENCODED]) // <11>
    @Status(CREATED) // <12>
    fun move(@PathVariable gameId: String,
             player: String,
             move: String,
             fen: String,
             pgn: String) {
        val gameState = GameStateDTO(UUID.randomUUID().toString(),
                gameId, player, move, fen, pgn)
        gameReporter.gameState(gameId, gameState).subscribe() // <13>
    }

    @Post("/checkmate/{gameId}/{player}") // <14>
    @Status(NO_CONTENT) // <15>
    fun checkmate(@PathVariable gameId: String,
                  @PathVariable player: String) {
        val game = GameDTO(gameId, false, player)
        gameReporter.game(gameId, game).subscribe() // <16>
    }

    @Post("/draw/{gameId}") // <17>
    @Status(NO_CONTENT) // <18>
    fun draw(@PathVariable gameId: String) {
        val game = GameDTO(gameId, true, null)
        gameReporter.game(gameId, game).subscribe() // <19>
    }
}
