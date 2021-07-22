package example.micronaut.chess.repository

import example.micronaut.chess.entity.GameState
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

/**
 * <code>GameState</code> entity repository.
 */
@Repository
interface GameStateRepository extends CrudRepository<GameState, UUID> {
}
