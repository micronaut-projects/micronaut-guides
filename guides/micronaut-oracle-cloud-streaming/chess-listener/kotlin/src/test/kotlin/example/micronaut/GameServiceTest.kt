package example.micronaut

import example.micronaut.chess.dto.GameDTO
import example.micronaut.chess.dto.GameStateDTO
import example.micronaut.chess.entity.GameState
import example.micronaut.chess.repository.GameRepository
import example.micronaut.chess.repository.GameStateRepository
import io.micronaut.configuration.kafka.annotation.KafkaClient
import io.micronaut.configuration.kafka.annotation.KafkaKey
import io.micronaut.configuration.kafka.annotation.Topic
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import reactor.core.publisher.Mono
import java.util.UUID
import java.util.concurrent.TimeUnit.SECONDS
import jakarta.inject.Inject

@MicronautTest
@TestInstance(PER_CLASS) // <2>
class GameServiceTest { // <3>

    @Inject
    lateinit var gameReporter: GameReporter // <5>

    @Inject
    lateinit var gameRepository: GameRepository

    @Inject
    lateinit var gameStateRepository: GameStateRepository

    @Test
    fun testGameEndingInCheckmate() {

        val blackName = "b_name"
        val whiteName = "w_name"

        // start game
        val gameId = UUID.randomUUID()
        val gameIdString = gameId.toString()
        var gameDto = GameDTO(gameIdString, blackName, whiteName)

        gameReporter.game(gameIdString, gameDto).subscribe()

        await().atMost(5, SECONDS).until { gameRepository.count() > 0 } // <6>

        assertEquals(1, gameRepository.count())
        assertEquals(0, gameStateRepository.count())

        var game = gameRepository.findById(gameId).orElseThrow { IllegalStateException("Unable to find expected Game") }

        assertEquals(gameId, game.id)
        assertEquals(blackName, game.blackName)
        assertEquals(whiteName, game.whiteName)
        assertFalse(game.draw)
        assertNull(game.winner)

        // make moves
        val gameStateIds = mutableListOf<UUID>()

        var gameStateId = makeMove(gameIdString, "w", "f3", "rnbqkbnr/pppppppp/8/8/8/5P2/PPPPP1PP/RNBQKBNR b KQkq - 0 1", "1. f3")
        gameStateIds.add(gameStateId)

        gameStateId = makeMove(gameIdString, "b", "e6", "rnbqkbnr/pppp1ppp/4p3/8/8/5P2/PPPPP1PP/RNBQKBNR w KQkq - 0 2", "1. f3 e6")
        gameStateIds.add(gameStateId)

        gameStateId = makeMove(gameIdString, "w", "g4", "rnbqkbnr/pppp1ppp/4p3/8/6P1/5P2/PPPPP2P/RNBQKBNR b KQkq g3 0 2", "1. f3 e6 2. g4")
        gameStateIds.add(gameStateId)

        gameStateId = makeMove(gameIdString, "b", "Qh4#", "rnb1kbnr/pppp1ppp/4p3/8/6Pq/5P2/PPPPP2P/RNBQKBNR w KQkq - 1 3", "1. f3 e6 2. g4 Qh4#")
        gameStateIds.add(gameStateId)

        await().atMost(5, SECONDS).until { gameStateRepository.count() > 3 }

        assertEquals(1, gameRepository.count())
        assertEquals(4, gameStateRepository.count())

        val moves = mutableListOf<GameState>()
        for (id in gameStateIds) {
            moves.add(gameStateRepository.findById(id).orElseThrow { IllegalStateException("Unable to find expected GameState") }!!)
        }

        assertEquals("w", moves[0].player)
        assertEquals("f3", moves[0].move)

        assertEquals("b", moves[1].player)
        assertEquals("e6", moves[1].move)

        assertEquals("w", moves[2].player)
        assertEquals("g4", moves[2].move)

        assertEquals("b", moves[3].player)
        assertEquals("Qh4#", moves[3].move)

        // end game
        gameDto = GameDTO(gameIdString, false, "b")
        gameReporter.game(gameIdString, gameDto).subscribe()

        await().atMost(5, SECONDS).until {
            val g = gameRepository.findById(gameId).orElse(null) ?: return@until false
            g.winner != null
        }

        assertEquals(1, gameRepository.count())
        assertEquals(4, gameStateRepository.count())

        game = gameRepository.findById(gameId).orElseThrow { IllegalStateException("Unable to find expected Game") }

        assertEquals(gameId, game.id)
        assertEquals(blackName, game.blackName)
        assertEquals(whiteName, game.whiteName)
        assertFalse(game.draw)
        assertEquals("b", game.winner)
    }

    @Test
    fun testGameEndingInDraw() {

        val blackName = "b_name"
        val whiteName = "w_name"

        // start game
        val gameId = UUID.randomUUID()
        val gameIdString = gameId.toString()
        var gameDto = GameDTO(gameIdString, blackName, whiteName)

        gameReporter.game(gameIdString, gameDto).subscribe()

        await().atMost(5, SECONDS).until { gameRepository.count() > 0 } // <6>

        assertEquals(1, gameRepository.count())
        assertEquals(0, gameStateRepository.count())

        var game = gameRepository.findById(gameId).orElseThrow {
            IllegalStateException("Unable to find expected Game") }

        assertEquals(gameId, game.id)
        assertEquals(blackName, game.blackName)
        assertEquals(whiteName, game.whiteName)
        assertFalse(game.draw)
        assertNull(game.winner)

        // make moves
        val gameStateIds = mutableListOf<UUID>()

        var gameStateId = makeMove(gameIdString, "w", "f3", "rnbqkbnr/pppppppp/8/8/8/5P2/PPPPP1PP/RNBQKBNR b KQkq - 0 1", "1. f3")
        gameStateIds.add(gameStateId)

        gameStateId = makeMove(gameIdString, "b", "e6", "rnbqkbnr/pppp1ppp/4p3/8/8/5P2/PPPPP1PP/RNBQKBNR w KQkq - 0 2", "1. f3 e6")
        gameStateIds.add(gameStateId)

        await().atMost(5, SECONDS).until { gameStateRepository.count() > 1 }

        assertEquals(1, gameRepository.count())
        assertEquals(2, gameStateRepository.count())

        val moves = mutableListOf<GameState>()
        for (id in gameStateIds) {
            moves.add(gameStateRepository.findById(id).orElseThrow { IllegalStateException("Unable to find expected GameState") }!!)
        }

        assertEquals("w", moves[0].player)
        assertEquals("f3", moves[0].move)

        assertEquals("b", moves[1].player)
        assertEquals("e6", moves[1].move)

        // end game
        gameDto = GameDTO(gameIdString, true, null)
        gameReporter.game(gameIdString, gameDto).subscribe()

        await().atMost(5, SECONDS).until {
            val g = gameRepository.findById(gameId).orElse(null) ?: return@until false
            g.draw
        }

        assertEquals(1, gameRepository.count())
        assertEquals(2, gameStateRepository.count())

        game = gameRepository.findById(gameId).orElseThrow { IllegalStateException("Unable to find expected Game") }

        assertEquals(gameId, game.id)
        assertEquals(blackName, game.blackName)
        assertEquals(whiteName, game.whiteName)
        assertTrue(game.draw)
        assertNull(game.winner)
    }

    @AfterEach
    fun cleanup() {
        gameStateRepository.deleteAll()
        gameRepository.deleteAll()
    }

    @KafkaClient
    interface GameReporter {
        @Topic("chessGame")
        fun game(@KafkaKey gameId: String, game: GameDTO): Mono<GameDTO>

        @Topic("chessGameState")
        fun gameState(@KafkaKey gameId: String, gameState: GameStateDTO): Mono<GameStateDTO>
    }

    private fun makeMove(gameId: String, player: String, move: String,
                         fen: String, pgn: String): UUID {
        val gameStateId = UUID.randomUUID()
        gameReporter.gameState(gameId, GameStateDTO(gameStateId.toString(),
            gameId, player, move, fen, pgn)).subscribe()
        return gameStateId
    }
}
