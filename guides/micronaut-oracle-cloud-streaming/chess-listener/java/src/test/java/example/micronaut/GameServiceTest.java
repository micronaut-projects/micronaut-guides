package example.micronaut;

import example.micronaut.chess.dto.GameDTO;
import example.micronaut.chess.dto.GameStateDTO;
import example.micronaut.chess.entity.Game;
import example.micronaut.chess.entity.GameState;
import example.micronaut.chess.repository.GameRepository;
import example.micronaut.chess.repository.GameStateRepository;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import io.reactivex.Single;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@Testcontainers // <1>
@MicronautTest
@TestInstance(PER_CLASS) // <2>
class GameServiceTest implements TestPropertyProvider { // <3>

    @Container
    static KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:latest")); // <4>

    @Inject
    GameReporter gameReporter; // <5>

    @Inject
    GameRepository gameRepository;

    @Inject
    GameStateRepository gameStateRepository;

    @Test
    void testGameEndingInCheckmate() {

        String blackName = "b_name";
        String whiteName = "w_name";

        // start game

        UUID gameId = UUID.randomUUID();
        String gameIdString = gameId.toString();

        GameDTO gameDto = new GameDTO(gameIdString, blackName, whiteName);

        gameReporter.game(gameIdString, gameDto).subscribe();

        await().atMost(5, SECONDS).until(() -> gameRepository.count() > 0); // <6>

        assertEquals(1, gameRepository.count());
        assertEquals(0, gameStateRepository.count());

        Game game = gameRepository.findById(gameId).orElseThrow(() ->
                new IllegalStateException("Unable to find expected Game"));

        assertEquals(gameId, game.getId());
        assertEquals(blackName, game.getBlackName());
        assertEquals(whiteName, game.getWhiteName());
        assertFalse(game.isDraw());
        assertNull(game.getWinner());

        // make moves

        List<UUID> gameStateIds = new ArrayList<>();

        UUID gameStateId = makeMove(gameIdString, "w", "f3", "rnbqkbnr/pppppppp/8/8/8/5P2/PPPPP1PP/RNBQKBNR b KQkq - 0 1", "1. f3");
        gameStateIds.add(gameStateId);

        gameStateId = makeMove(gameIdString, "b", "e6", "rnbqkbnr/pppp1ppp/4p3/8/8/5P2/PPPPP1PP/RNBQKBNR w KQkq - 0 2", "1. f3 e6");
        gameStateIds.add(gameStateId);

        gameStateId = makeMove(gameIdString, "w", "g4", "rnbqkbnr/pppp1ppp/4p3/8/6P1/5P2/PPPPP2P/RNBQKBNR b KQkq g3 0 2", "1. f3 e6 2. g4");
        gameStateIds.add(gameStateId);

        gameStateId = makeMove(gameIdString, "b", "Qh4#", "rnb1kbnr/pppp1ppp/4p3/8/6Pq/5P2/PPPPP2P/RNBQKBNR w KQkq - 1 3", "1. f3 e6 2. g4 Qh4#");
        gameStateIds.add(gameStateId);

        await().atMost(5, SECONDS).until(() -> gameStateRepository.count() > 3);

        assertEquals(1, gameRepository.count());
        assertEquals(4, gameStateRepository.count());

        List<GameState> moves = new ArrayList<>();
        for (UUID id : gameStateIds) {
            moves.add(gameStateRepository.findById(id).orElseThrow(() ->
                    new IllegalStateException("Unable to find expected GameState")));
        }

        assertEquals("w", moves.get(0).getPlayer());
        assertEquals("f3", moves.get(0).getMove());

        assertEquals("b", moves.get(1).getPlayer());
        assertEquals("e6", moves.get(1).getMove());

        assertEquals("w", moves.get(2).getPlayer());
        assertEquals("g4", moves.get(2).getMove());

        assertEquals("b", moves.get(3).getPlayer());
        assertEquals("Qh4#", moves.get(3).getMove());

        // end game

        gameDto = new GameDTO(gameIdString, false, "b");
        gameReporter.game(gameIdString, gameDto).subscribe();

        await().atMost(5, SECONDS).until(() -> {
            Game g = gameRepository.findById(gameId).orElse(null);
            if (g == null) return false;
            return g.getWinner() != null;
        });

        assertEquals(1, gameRepository.count());
        assertEquals(4, gameStateRepository.count());

        game = gameRepository.findById(gameId).orElseThrow(() ->
                new IllegalStateException("Unable to find expected Game"));

        assertEquals(gameId, game.getId());
        assertEquals(blackName, game.getBlackName());
        assertEquals(whiteName, game.getWhiteName());
        assertFalse(game.isDraw());
        assertEquals("b", game.getWinner());
    }

    @Test
    void testGameEndingInDraw() {

        String blackName = "b_name";
        String whiteName = "w_name";

        // start game

        UUID gameId = UUID.randomUUID();
        String gameIdString = gameId.toString();
        GameDTO gameDto = new GameDTO(gameIdString, blackName, whiteName);

        gameReporter.game(gameIdString, gameDto).subscribe();

        await().atMost(5, SECONDS).until(() -> gameRepository.count() > 0);

        assertEquals(1, gameRepository.count());
        assertEquals(0, gameStateRepository.count());

        Game game = gameRepository.findById(gameId).orElseThrow(() ->
                new IllegalStateException("Unable to find expected Game"));

        assertEquals(gameId, game.getId());
        assertEquals(blackName, game.getBlackName());
        assertEquals(whiteName, game.getWhiteName());
        assertFalse(game.isDraw());
        assertNull(game.getWinner());

        // make moves

        List<UUID> gameStateIds = new ArrayList<>();

        UUID gameStateId = makeMove(gameIdString, "w", "f3", "rnbqkbnr/pppppppp/8/8/8/5P2/PPPPP1PP/RNBQKBNR b KQkq - 0 1", "1. f3");
        gameStateIds.add(gameStateId);

        gameStateId = makeMove(gameIdString, "b", "e6", "rnbqkbnr/pppp1ppp/4p3/8/8/5P2/PPPPP1PP/RNBQKBNR w KQkq - 0 2", "1. f3 e6");
        gameStateIds.add(gameStateId);

        await().atMost(5, SECONDS).until(() -> gameStateRepository.count() > 1);

        assertEquals(1, gameRepository.count());
        assertEquals(2, gameStateRepository.count());

        List<GameState> moves = new ArrayList<>();
        for (UUID id : gameStateIds) {
            moves.add(gameStateRepository.findById(id).orElseThrow(() ->
                    new IllegalStateException("Unable to find expected GameState")));
        }

        assertEquals("w", moves.get(0).getPlayer());
        assertEquals("f3", moves.get(0).getMove());

        assertEquals("b", moves.get(1).getPlayer());
        assertEquals("e6", moves.get(1).getMove());

        // end game

        gameDto = new GameDTO(gameIdString, true, null);
        gameReporter.game(gameIdString, gameDto).subscribe();

        await().atMost(5, SECONDS).until(() -> {
            Game g = gameRepository.findById(gameId).orElse(null);
            if (g == null) return false;
            return g.isDraw();
        });

        assertEquals(1, gameRepository.count());
        assertEquals(2, gameStateRepository.count());

        game = gameRepository.findById(gameId).orElseThrow(() ->
                new IllegalStateException("Unable to find expected Game"));

        assertEquals(gameId, game.getId());
        assertEquals(blackName, game.getBlackName());
        assertEquals(whiteName, game.getWhiteName());
        assertTrue(game.isDraw());
        assertNull(game.getWinner());
    }

    @NonNull
    @Override
    public Map<String, String> getProperties() {
        return Collections.singletonMap(
                "kafka.bootstrap.servers", kafka.getBootstrapServers() // <7>
        );
    }

    @AfterEach
    void cleanup() {
        gameStateRepository.deleteAll();
        gameRepository.deleteAll();
    }

    @KafkaClient
    interface GameReporter {

        @Topic("chessGame")
        Single<GameDTO> game(@KafkaKey String gameId, GameDTO game);

        @Topic("chessGameState")
        Single<GameStateDTO> gameState(@KafkaKey String gameId, GameStateDTO gameState);
    }

    private UUID makeMove(String gameId, String player, String move,
                          String fen, String pgn) {
        UUID gameStateId = UUID.randomUUID();
        gameReporter.gameState(gameId, new GameStateDTO(gameStateId.toString(),
                gameId, player, move, fen, pgn)).subscribe();
        return gameStateId;
    }
}
