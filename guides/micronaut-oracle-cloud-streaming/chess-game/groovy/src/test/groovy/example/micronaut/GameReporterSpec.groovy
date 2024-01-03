/*
 * Copyright 2017-2024 original authors
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
package example.micronaut

import example.micronaut.chess.dto.Player
import example.micronaut.chess.dto.GameDTO
import example.micronaut.chess.dto.GameStateDTO
import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.Topic
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.utility.DockerImageName
import spock.lang.Specification
import spock.util.concurrent.PollingConditions
import io.micronaut.http.uri.UriBuilder
import jakarta.inject.Inject
import java.util.concurrent.ConcurrentLinkedDeque
import static io.micronaut.configuration.kafka.annotation.OffsetReset.EARLIEST
import static io.micronaut.http.MediaType.APPLICATION_FORM_URLENCODED_TYPE

@MicronautTest // <1> <2>
class GameReporterSpec extends Specification implements TestPropertyProvider { // <3>

    private static final Collection<GameDTO> receivedGames = new ConcurrentLinkedDeque<>()
    private static final Collection<GameStateDTO> receivedMoves = new ConcurrentLinkedDeque<>()

    private static final PollingConditions pollingConditions = new PollingConditions(timeout: 5)

    static KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse('confluentinc/cp-kafka:latest')) // <4>

    @Inject
    ChessListener chessListener // <5>

    @Inject
    @Client('/')
    HttpClient client // <6>

    void 'test game ending in checkmate'() {

        given:
        String blackName = 'b_name'
        String whiteName = 'w_name'

        when: 'start game'
        Optional<String> result = startGame(blackName, whiteName)
        String gameId = result.orElseThrow(() -> new RuntimeException('Expected GameDTO id'))

        then:
        pollingConditions.eventually { // <7>
            !receivedGames.empty
        }

        1 == receivedGames.size()
        0 == receivedMoves.size()

        when:
        GameDTO game = receivedGames[0]

        then:
        gameId == game.id
        blackName == game.blackName
        whiteName == game.whiteName
        !game.draw
        !game.winner

        when: 'make moves'
        receivedGames.clear()

        makeMove(gameId, Player.WHITE, 'f3', 'rnbqkbnr/pppppppp/8/8/8/5P2/PPPPP1PP/RNBQKBNR b KQkq - 0 1', '1. f3')
        makeMove(gameId, Player.BLACK, 'e6', 'rnbqkbnr/pppp1ppp/4p3/8/8/5P2/PPPPP1PP/RNBQKBNR w KQkq - 0 2', '1. f3 e6')
        makeMove(gameId, Player.WHITE, 'g4', 'rnbqkbnr/pppp1ppp/4p3/8/6P1/5P2/PPPPP2P/RNBQKBNR b KQkq g3 0 2', '1. f3 e6 2. g4')
        makeMove(gameId, Player.BLACK, 'Qh4#', 'rnb1kbnr/pppp1ppp/4p3/8/6Pq/5P2/PPPPP2P/RNBQKBNR w KQkq - 1 3', '1. f3 e6 2. g4 Qh4#')

        then:
        pollingConditions.eventually {
            receivedMoves.size() > 3
        }

        0 == receivedGames.size()
        4 == receivedMoves.size()

        when:
        List<GameStateDTO> moves = new ArrayList<>(receivedMoves)

        then:
        Player.WHITE == moves[0].player
        'f3' == moves[0].move

        Player.BLACK == moves[1].player
        'e6' == moves[1].move

        Player.WHITE == moves[2].player
        'g4' == moves[2].move

        Player.BLACK == moves[3].player
        'Qh4#' == moves[3].move

        when: 'end game'

        receivedMoves.clear()

        endGame(gameId, Player.BLACK)

        then:
        pollingConditions.eventually {
            !receivedGames.empty
        }

        1 == receivedGames.size()
        0 == receivedMoves.size()

        when:
        game = receivedGames[0]

        then:
        gameId == game.id
        !game.blackName
        !game.whiteName
        !game.draw
        Player.BLACK == game.winner
    }

    void 'test game ending in draw'() {

        given:
        String blackName = 'b_name'
        String whiteName = 'w_name'

        when: 'start game'
        Optional<String> result = startGame(blackName, whiteName)
        String gameId = result.orElseThrow(() -> new RuntimeException('Expected GameDTO id'))

        then:
        pollingConditions.eventually {
            !receivedGames.empty
        }

        1 == receivedGames.size()
        0 == receivedMoves.size()

        when:
        GameDTO game = receivedGames[0]

        then:
        gameId == game.id
        blackName == game.blackName
        whiteName == game.whiteName
        !game.draw
        !game.winner

        when: 'make moves'
        receivedGames.clear()

        makeMove(gameId, Player.WHITE, 'f3', 'rnbqkbnr/pppppppp/8/8/8/5P2/PPPPP1PP/RNBQKBNR b KQkq - 0 1', '1. f3')
        makeMove(gameId, Player.BLACK, 'e6', 'rnbqkbnr/pppp1ppp/4p3/8/8/5P2/PPPPP1PP/RNBQKBNR w KQkq - 0 2', '1. f3' +
                'Player')

        then:
        pollingConditions.eventually {
            receivedMoves.size() > 1
        }

        0 == receivedGames.size()
        2 == receivedMoves.size()

        when: 'end game'
        receivedMoves.clear()
        endGame(gameId, null)

        then:
        new PollingConditions().eventually {
            !receivedGames.empty
        }

        1 == receivedGames.size()
        0 == receivedMoves.size()

        when:
        game = receivedGames[0]

        then:
        gameId == game.id
        !game.blackName
        !game.whiteName
        game.draw
        !game.winner
        Player
    }


    @Override
    Map<String, String> getProperties() {
        kafka.start()
        ['kafka.bootstrap.servers': kafka.bootstrapServers] // <8>
    }

    void cleanup() {
        receivedGames.clear()
        receivedMoves.clear()
    }

    @KafkaListener(offsetReset = EARLIEST)
    static class ChessListener {

        @Topic('chessGame')
        void onGame(GameDTO game) {
            receivedGames << game
        }

        @Topic('chessGameState')
        void onGameState(GameStateDTO gameState) {
            receivedMoves << gameState
        }
    }

    private Optional<String> startGame(String blackName, String whiteName) {
        Map body = [b: blackName, w: whiteName] // <9>

        HttpRequest<?> request = HttpRequest.POST('/game/start', body)
                .contentType(APPLICATION_FORM_URLENCODED_TYPE)
        return client.toBlocking().retrieve(request,
                Argument.of(Optional, String)) // <10>
    }

    private void makeMove(String gameId,
                          Player player,
                          String move,
                          String fen, String pgn) {
        Map body = [player: player, move: move, fen: fen, pgn: pgn]

        HttpRequest<?> request = HttpRequest.POST('/game/move/' + gameId, body)
                .contentType(APPLICATION_FORM_URLENCODED_TYPE)
        client.toBlocking().exchange(request) // <11>
    }

    private void endGame(String gameId, Player winner) {
        UriBuilder uriBuilder = UriBuilder.of("/game").path(winner == null ? "draw" : "checkmate").path(gameId)
        if (winner != null) {
            uriBuilder = uriBuilder.path(winner.toString())
        }
        URI uri = uriBuilder.build()
        HttpRequest<Object> request = HttpRequest.POST(uri, null)
        client.toBlocking().exchange(request) // <12>
    }
}
