package example.micronaut.chess.repository

import example.micronaut.chess.entity.Game
import io.micronaut.data.repository.CrudRepository
import java.util.UUID

interface GameRepository : CrudRepository<Game, UUID>
