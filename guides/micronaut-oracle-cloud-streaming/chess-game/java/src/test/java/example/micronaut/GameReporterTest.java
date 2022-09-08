package example.micronaut;

import example.micronaut.chess.dto.Player;
import example.micronaut.chess.dto.GameDTO;
import example.micronaut.chess.dto.GameStateDTO;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.net.URI;
import io.micronaut.http.uri.UriBuilder;
import static io.micronaut.configuration.kafka.annotation.OffsetReset.EARLIEST;
import static io.micronaut.http.MediaType.APPLICATION_FORM_URLENCODED_TYPE;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@MicronautTest
@TestInstance(PER_CLASS) // <2>
class GameReporterTest { // <3>

    private static final Collection<GameDTO> receivedGames = new ConcurrentLinkedDeque<>();
    private static final Collection<GameStateDTO> receivedMoves = new ConcurrentLinkedDeque<>();

    @Inject
    ChessListener chessListener; // <5>

    @Inject
    @Client("/")
    HttpClient client; // <6>

    @Test
    void testGameEndingInCheckmate() {

        String blackName = "b_name";
        String whiteName = "w_name";

        // start game

        Optional<String> result = startGame(blackName, whiteName);
        String gameId = result.orElseThrow(() -> new RuntimeException("Expected GameDTO id"));

        await().atMost(5, SECONDS).until(() -> !receivedGames.isEmpty()); // <7>

        assertEquals(1, receivedGames.size());
        assertEquals(0, receivedMoves.size());

        GameDTO game = receivedGames.iterator().next();

        assertEquals(gameId, game.getId());
        assertEquals(blackName, game.getBlackName());
        assertEquals(whiteName, game.getWhiteName());
        assertFalse(game.isDraw());
        assertNull(game.getWinner());

        // make moves
        receivedGames.clear();

        makeMove(gameId, Player.WHITE, "f3", "rnbqkbnr/pppppppp/8/8/8/5P2/PPPPP1PP/RNBQKBNR b KQkq - 0 1", "1. f3");
        makeMove(gameId, Player.BLACK, "e6", "rnbqkbnr/pppp1ppp/4p3/8/8/5P2/PPPPP1PP/RNBQKBNR w KQkq - 0 2", "1. f3 e6");
        makeMove(gameId, Player.WHITE, "g4", "rnbqkbnr/pppp1ppp/4p3/8/6P1/5P2/PPPPP2P/RNBQKBNR b KQkq g3 0 2", "1. f3 e6 2. g4");
        makeMove(gameId, Player.BLACK, "Qh4#", "rnb1kbnr/pppp1ppp/4p3/8/6Pq/5P2/PPPPP2P/RNBQKBNR w KQkq - 1 3", "1. f3 e6 2. g4 Qh4#");

        await().atMost(5, SECONDS).until(() -> receivedMoves.size() > 3);

        assertEquals(0, receivedGames.size());
        assertEquals(4, receivedMoves.size());

        List<GameStateDTO> moves = new ArrayList<>(receivedMoves);

        assertEquals(Player.WHITE, moves.get(0).getPlayer());
        assertEquals("f3", moves.get(0).getMove());

        assertEquals(Player.BLACK, moves.get(1).getPlayer());
        assertEquals("e6", moves.get(1).getMove());

        assertEquals(Player.WHITE, moves.get(2).getPlayer());
        assertEquals("g4", moves.get(2).getMove());

        assertEquals(Player.BLACK, moves.get(3).getPlayer());
        assertEquals("Qh4#", moves.get(3).getMove());

        // end game

        receivedMoves.clear();

        endGame(gameId, Player.BLACK);

        await().atMost(5, SECONDS).until(() -> !receivedGames.isEmpty());

        assertEquals(1, receivedGames.size());
        assertEquals(0, receivedMoves.size());

        game = receivedGames.iterator().next();

        assertEquals(gameId, game.getId());
        assertNull(game.getBlackName());
        assertNull(game.getWhiteName());
        assertFalse(game.isDraw());
        assertEquals(Player.BLACK, game.getWinner());
    }

    @Test
    void testGameEndingInDraw() {

        String blackName = "b_name";
        String whiteName = "w_name";

        // start game

        Optional<String> result = startGame(blackName, whiteName);

        String gameId = result.orElseThrow(() -> new RuntimeException("Expected GameDTO id"));

        await().atMost(5, SECONDS).until(() -> !receivedGames.isEmpty());

        assertEquals(1, receivedGames.size());
        assertEquals(0, receivedMoves.size());

        GameDTO game = receivedGames.iterator().next();

        assertEquals(gameId, game.getId());
        assertEquals(blackName, game.getBlackName());
        assertEquals(whiteName, game.getWhiteName());
        assertFalse(game.isDraw());
        assertNull(game.getWinner());

        // make moves
        receivedGames.clear();

        makeMove(gameId, Player.WHITE, "f3", "rnbqkbnr/pppppppp/8/8/8/5P2/PPPPP1PP/RNBQKBNR b KQkq - 0 1", "1. f3");
        makeMove(gameId, Player.BLACK, "e6", "rnbqkbnr/pppp1ppp/4p3/8/8/5P2/PPPPP1PP/RNBQKBNR w KQkq - 0 2", "1. f3 e6");

        await().atMost(5, SECONDS).until(() -> receivedMoves.size() > 1);

        assertEquals(0, receivedGames.size());
        assertEquals(2, receivedMoves.size());

        // end game

        receivedMoves.clear();

        endGame(gameId, null);

        await().atMost(5, SECONDS).until(() -> !receivedGames.isEmpty());

        assertEquals(1, receivedGames.size());
        assertEquals(0, receivedMoves.size());

        game = receivedGames.iterator().next();

        assertEquals(gameId, game.getId());
        assertNull(game.getBlackName());
        assertNull(game.getWhiteName());
        assertTrue(game.isDraw());
        assertNull(game.getWinner());
    }

    @AfterEach
    void cleanup() {
        receivedGames.clear();
        receivedMoves.clear();
    }

    @KafkaListener(offsetReset = EARLIEST)
    static class ChessListener {

        @Topic("chessGame")
        void onGame(GameDTO game) {
            receivedGames.add(game);
        }

        @Topic("chessGameState")
        void onGameState(GameStateDTO gameState) {
            receivedMoves.add(gameState);
        }
    }

    private Optional<String> startGame(String blackName, String whiteName) {
        Map<String, String> body = new HashMap<>(); // <9>
        body.put(Player.BLACK.toString(), blackName);
        body.put(Player.WHITE.toString(), whiteName);

        HttpRequest<?> request = HttpRequest.POST("/game/start", body)
                .contentType(APPLICATION_FORM_URLENCODED_TYPE);
        return client.toBlocking().retrieve(request,
                Argument.of(Optional.class, String.class)); // <10>
    }

    private void makeMove(String gameId, Player player, String move,
                          String fen, String pgn) {
        Map<String, String> body = new HashMap<>();
        body.put("player", player.toString());
        body.put("move", move);
        body.put("fen", fen);
        body.put("pgn", pgn);

        HttpRequest<?> request = HttpRequest.POST("/game/move/" + gameId, body)
                .contentType(APPLICATION_FORM_URLENCODED_TYPE);
        client.toBlocking().exchange(request); // <11>
    }

    private void endGame(String gameId, Player winner) {
        UriBuilder uriBuilder = UriBuilder.of("/game").path(winner == null ? "draw" : "checkmate").path(gameId);
        if (winner != null) {
            uriBuilder = uriBuilder.path(winner.toString());
        }
        URI uri = uriBuilder.build();
        HttpRequest<?> request = HttpRequest.POST(uri, null);
        client.toBlocking().exchange(request); // <12>
    }
}
