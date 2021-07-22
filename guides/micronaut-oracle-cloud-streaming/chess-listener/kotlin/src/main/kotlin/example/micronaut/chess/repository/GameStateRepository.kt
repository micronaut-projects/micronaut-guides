package example.micronaut.chess.repository

import example.micronaut.chess.entity.GameState
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository
import java.util.UUID

/**
 * `GameState` entity repository.
 */
@Repository
interface GameStateRepository : CrudRepository<GameState, UUID>
