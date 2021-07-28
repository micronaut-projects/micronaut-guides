package example.micronaut

import example.micronaut.chess.dto.GameDTO
import example.micronaut.chess.dto.GameStateDTO
import example.micronaut.chess.entity.Game
import example.micronaut.chess.entity.GameState
import example.micronaut.chess.repository.GameRepository
import example.micronaut.chess.repository.GameStateRepository
import io.micronaut.configuration.kafka.annotation.KafkaClient
import io.micronaut.configuration.kafka.annotation.KafkaKey
import io.micronaut.configuration.kafka.annotation.Topic
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.utility.DockerImageName
import reactor.core.publisher.Mono
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

import javax.inject.Inject

@MicronautTest // <1> <2>
class GameServiceSpec extends Specification implements TestPropertyProvider { // <3>

    private static final PollingConditions pollingConditions = new PollingConditions(timeout: 30)

    static KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse('confluentinc/cp-kafka:latest')) // <4>

    @Inject
    GameReporter gameReporter // <5>

    @Inject
    GameRepository gameRepository

    @Inject
    GameStateRepository gameStateRepository

    void 'test game ending in checkmate'() {
        given:
        String blackName = 'b_name'
        String whiteName = 'w_name'

        when: 'start game'

        UUID gameId = UUID.randomUUID()
        String gameIdString = gameId
        GameDTO gameDto = new GameDTO(gameIdString, blackName, whiteName)

        gameReporter.game(gameIdString, gameDto).subscribe()

        then:
        pollingConditions.eventually { // <6>
            gameRepository.count() > 0
        }

        1 == gameRepository.count()
        0 == gameStateRepository.count()

        when:
        Game game = gameRepository.findById(gameId).orElseThrow(() ->
                new IllegalStateException('Unable to find expected Game'))

        then:
        gameId == game.id
        blackName == game.blackName
        whiteName == game.whiteName
        !game.draw
        !game.winner

        when: 'make moves'

        List<UUID> gameStateIds = []

        UUID gameStateId = makeMove(gameIdString, 'w', 'f3', 'rnbqkbnr/pppppppp/8/8/8/5P2/PPPPP1PP/RNBQKBNR b KQkq - 0 1', '1. f3')
        gameStateIds << gameStateId

        gameStateId = makeMove(gameIdString, 'b', 'e6', 'rnbqkbnr/pppp1ppp/4p3/8/8/5P2/PPPPP1PP/RNBQKBNR w KQkq - 0 2', '1. f3 e6')
        gameStateIds << gameStateId

        gameStateId = makeMove(gameIdString, 'w', 'g4', 'rnbqkbnr/pppp1ppp/4p3/8/6P1/5P2/PPPPP2P/RNBQKBNR b KQkq g3 0 2', '1. f3 e6 2. g4')
        gameStateIds << gameStateId

        gameStateId = makeMove(gameIdString, 'b', 'Qh4#', 'rnb1kbnr/pppp1ppp/4p3/8/6Pq/5P2/PPPPP2P/RNBQKBNR w KQkq - 1 3', '1. f3 e6 2. g4 Qh4#')
        gameStateIds << gameStateId

        then:
        pollingConditions.eventually {
            gameStateRepository.count() > 3
        }

        1 == gameRepository.count()
        4 == gameStateRepository.count()

        when:
        List<GameState> moves = gameStateIds.collect { UUID id -> gameStateRepository.findById(id).orElse(null) }

        then:
        'w' == moves[0].player
        'f3' == moves[0].move

        'b' == moves[1].player
        'e6' == moves[1].move

        'w' == moves[2].player
        'g4' == moves[2].move

        'b' == moves[3].player
        'Qh4#' == moves[3].move

        when: 'end game'

        gameDto = new GameDTO(gameIdString, false, 'b')
        gameReporter.game(gameIdString, gameDto).subscribe()

        then:
        pollingConditions.eventually {
            Game g = gameRepository.findById(gameId).orElse(null)
            if (!g) return false
            g.winner
        }

        1 == gameRepository.count()
        4 == gameStateRepository.count()

        when:
        game = gameRepository.findById(gameId).orElseThrow(() ->
                new IllegalStateException('Unable to find expected Game'))

        then:
        gameId == game.id
        blackName == game.blackName
        whiteName == game.whiteName
        !game.draw
        'b' == game.winner
    }

    void 'test game ending in draw'() {
        given:
        String blackName = 'b_name'
        String whiteName = 'w_name'

        when: 'start game'

        UUID gameId = UUID.randomUUID()
        String gameIdString = gameId
        GameDTO gameDto = new GameDTO(gameIdString, blackName, whiteName)

        gameReporter.game(gameIdString, gameDto).subscribe()

        then:
        pollingConditions.eventually { // <6>
            gameRepository.count() > 0
        }

        1 == gameRepository.count()
        0 == gameStateRepository.count()

        when:
        Game game = gameRepository.findById(gameId).orElseThrow(() ->
                new IllegalStateException('Unable to find expected Game'))

        then:
        gameId == game.id
        blackName == game.blackName
        whiteName == game.whiteName
        !game.draw
        !game.winner

        when: 'make moves'

        List<UUID> gameStateIds = []

        UUID gameStateId = makeMove(gameIdString, 'w', 'f3', 'rnbqkbnr/pppppppp/8/8/8/5P2/PPPPP1PP/RNBQKBNR b KQkq - 0 1', '1. f3')
        gameStateIds << gameStateId

        gameStateId = makeMove(gameIdString, 'b', 'e6', 'rnbqkbnr/pppp1ppp/4p3/8/8/5P2/PPPPP1PP/RNBQKBNR w KQkq - 0 2', '1. f3 e6')
        gameStateIds << gameStateId

        then:
        pollingConditions.eventually {
            gameStateRepository.count() > 1
        }

        1 == gameRepository.count()
        2 == gameStateRepository.count()

        when:
        List<GameState> moves = []
        for (UUID id : gameStateIds) {
            moves << gameStateRepository.findById(id).orElseThrow(() ->
                    new IllegalStateException('Unable to find expected GameState'))
        }

        then:
        'w' == moves[0].player
        'f3' == moves[0].move

        'b' == moves[1].player
        'e6' == moves[1].move

        when: 'end game'

        gameDto = new GameDTO(gameIdString, true, null)
        gameReporter.game(gameIdString, gameDto).subscribe()

        then:
        pollingConditions.eventually {
            Game g = gameRepository.findById(gameId).orElse(null)
            if (!g) return false
            g.draw
        }

        1 == gameRepository.count()
        2 == gameStateRepository.count()

        when:
        game = gameRepository.findById(gameId).orElseThrow(() ->
                new IllegalStateException('Unable to find expected Game'))

        then:
        gameId == game.id
        blackName == game.blackName
        whiteName == game.whiteName
        game.draw
        !game.winner
    }

    @Override
    Map<String, String> getProperties() {
        kafka.start()
        ['kafka.bootstrap.servers': kafka.bootstrapServers] // <7>
    }

    void cleanup() {
        gameStateRepository.deleteAll()
        gameRepository.deleteAll()
    }

    @KafkaClient
    static interface GameReporter {

        @Topic('chessGame')
        Mono<GameDTO> game(@KafkaKey String gameId, GameDTO game)

        @Topic('chessGameState')
        Mono<GameStateDTO> gameState(@KafkaKey String gameId, GameStateDTO gameState)
    }

    private UUID makeMove(String gameId, String player, String move,
                          String fen, String pgn) {
        UUID gameStateId = UUID.randomUUID()
        gameReporter.gameState(gameId, new GameStateDTO(gameStateId.toString(),
                gameId, player, move, fen, pgn)).subscribe()
        gameStateId
    }
}
