package example.micronaut.chess.repository

import example.micronaut.chess.entity.Game
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository
import java.util.UUID

/**
 * `Game` entity repository.
 */
@Repository
interface GameRepository : CrudRepository<Game, UUID>
