package example.micronaut

import example.micronaut.chess.dto.GameDTO
import example.micronaut.chess.dto.GameStateDTO
import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.OffsetReset.EARLIEST
import io.micronaut.configuration.kafka.annotation.Topic
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.MediaType.APPLICATION_FORM_URLENCODED_TYPE
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import java.util.Optional
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.TimeUnit.SECONDS
import jakarta.inject.Inject

@Testcontainers // <1>
@MicronautTest
@TestInstance(PER_CLASS) // <2>
class GameReporterTest : TestPropertyProvider { // <3>

    companion object {
        val receivedGames: MutableCollection<GameDTO> = ConcurrentLinkedDeque()
        val receivedMoves: MutableCollection<GameStateDTO> = ConcurrentLinkedDeque()

        @Container
        val kafka = KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest")) // <4>
    }

    @Inject
    lateinit var chessListener: ChessListener // <5>

    @Inject // <6>
    @field:Client("/") // <7>
    lateinit var client: HttpClient // <8>

    @Test
    fun testGameEndingInCheckmate() {

        val blackName = "b_name"
        val whiteName = "w_name"

        // start game

        val result = startGame(blackName, whiteName)
        val gameId = result.orElseThrow { RuntimeException("Expected GameDTO id") }

        await().atMost(5, SECONDS).until { !receivedGames.isEmpty() } // <9>

        assertEquals(1, receivedGames.size)
        assertEquals(0, receivedMoves.size)

        var game = receivedGames.iterator().next()

        assertEquals(gameId, game.id)
        assertEquals(blackName, game.blackName)
        assertEquals(whiteName, game.whiteName)
        assertFalse(game.draw)
        assertNull(game.winner)

        // make moves
        receivedGames.clear()

        makeMove(gameId, "w", "f3", "rnbqkbnr/pppppppp/8/8/8/5P2/PPPPP1PP/RNBQKBNR b KQkq - 0 1", "1. f3")
        makeMove(gameId, "b", "e6", "rnbqkbnr/pppp1ppp/4p3/8/8/5P2/PPPPP1PP/RNBQKBNR w KQkq - 0 2", "1. f3 e6")
        makeMove(gameId, "w", "g4", "rnbqkbnr/pppp1ppp/4p3/8/6P1/5P2/PPPPP2P/RNBQKBNR b KQkq g3 0 2", "1. f3 e6 2. g4")
        makeMove(gameId, "b", "Qh4#", "rnb1kbnr/pppp1ppp/4p3/8/6Pq/5P2/PPPPP2P/RNBQKBNR w KQkq - 1 3", "1. f3 e6 2. g4 Qh4#")

        await().atMost(5, SECONDS).until { receivedMoves.size > 3 }

        assertEquals(0, receivedGames.size)
        assertEquals(4, receivedMoves.size)

        val moves: List<GameStateDTO> = ArrayList(receivedMoves)

        assertEquals("w", moves[0].player)
        assertEquals("f3", moves[0].move)

        assertEquals("b", moves[1].player)
        assertEquals("e6", moves[1].move)

        assertEquals("w", moves[2].player)
        assertEquals("g4", moves[2].move)

        assertEquals("b", moves[3].player)
        assertEquals("Qh4#", moves[3].move)

        // end game

        receivedMoves.clear()

        endGame(gameId, "b")

        await().atMost(5, SECONDS).until { !receivedGames.isEmpty() }

        assertEquals(1, receivedGames.size)
        assertEquals(0, receivedMoves.size)

        game = receivedGames.iterator().next()

        assertEquals(gameId, game.id)
        assertNull(game.blackName)
        assertNull(game.whiteName)
        assertFalse(game.draw)
        assertEquals("b", game.winner)
    }

    @Test
    fun testGameEndingInDraw() {

        val blackName = "b_name"
        val whiteName = "w_name"

        // start game
        val result = startGame(blackName, whiteName)
        val gameId = result.orElseThrow { RuntimeException("Expected GameDTO id") }

        await().atMost(5, SECONDS).until { !receivedGames.isEmpty() }

        assertEquals(1, receivedGames.size)
        assertEquals(0, receivedMoves.size)

        var game = receivedGames.iterator().next()

        assertEquals(gameId, game.id)
        assertEquals(blackName, game.blackName)
        assertEquals(whiteName, game.whiteName)
        assertFalse(game.draw)
        assertNull(game.winner)

        // make moves

        receivedGames.clear()

        makeMove(gameId, "w", "f3", "rnbqkbnr/pppppppp/8/8/8/5P2/PPPPP1PP/RNBQKBNR b KQkq - 0 1", "1. f3")
        makeMove(gameId, "b", "e6", "rnbqkbnr/pppp1ppp/4p3/8/8/5P2/PPPPP1PP/RNBQKBNR w KQkq - 0 2", "1. f3 e6")

        await().atMost(5, SECONDS).until { receivedMoves.size > 1 }

        assertEquals(0, receivedGames.size)
        assertEquals(2, receivedMoves.size)

        // end game

        receivedMoves.clear()

        endGame(gameId, null)

        await().atMost(5, SECONDS).until { !receivedGames.isEmpty() }

        assertEquals(1, receivedGames.size)
        assertEquals(0, receivedMoves.size)

        game = receivedGames.iterator().next()

        assertEquals(gameId, game.id)
        assertNull(game.blackName)
        assertNull(game.whiteName)
        assertTrue(game.draw)
        assertNull(game.winner)
    }

    override fun getProperties(): Map<String, String> {
        kafka.start()
        return mapOf("kafka.bootstrap.servers" to kafka.bootstrapServers) // <10>
    }

    @AfterEach
    fun cleanup() {
        receivedGames.clear()
        receivedMoves.clear()
    }

    @KafkaListener(offsetReset = EARLIEST)
    class ChessListener {
        @Topic("chessGame")
        fun onGame(game: GameDTO) {
            receivedGames.add(game)
        }

        @Topic("chessGameState")
        fun onGameState(gameState: GameStateDTO) {
            receivedMoves.add(gameState)
        }
    }

    private fun startGame(blackName: String, whiteName: String): Optional<String> {
        val body = mapOf("b" to blackName, "w" to whiteName) // <11>
        val request: HttpRequest<*> = HttpRequest.POST("/game/start", body)
                .contentType(APPLICATION_FORM_URLENCODED_TYPE)
        return client.toBlocking().retrieve(request,
                Argument.of(Optional::class.java, String::class.java)) as Optional<String> // <12>
    }

    private fun makeMove(gameId: String, player: String, move: String,
                         fen: String, pgn: String) {

        val body = mapOf("player" to player, "move" to move, "fen" to fen, "pgn" to pgn)
        val request = HttpRequest.POST("/game/move/$gameId", body)
                .contentType(APPLICATION_FORM_URLENCODED_TYPE)
        client.toBlocking().exchange<Map<String, String>, Any>(request) // <13>

    }

    private fun endGame(gameId: String, winner: String?) {
        val uri = if (winner == null) "/game/draw/$gameId" else "/game/checkmate/$gameId/$winner"
        val request: HttpRequest<Any?> = HttpRequest.POST(uri, null)
        client.toBlocking().exchange<Any?, Any>(request) // <14>
    }
}
