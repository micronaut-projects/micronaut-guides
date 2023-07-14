package example.micronaut.chess.repository

import example.micronaut.chess.entity.GameState
import io.micronaut.core.annotation.NonNull
import io.micronaut.data.annotation.Join
import io.micronaut.data.annotation.Join.Type.FETCH
import io.micronaut.data.repository.CrudRepository
import java.util.Optional
import java.util.UUID
import jakarta.validation.constraints.NotNull

interface GameStateRepository : CrudRepository<GameState, UUID> {

    @NonNull
    @Join(value = "game", type = FETCH) // <1>
    override fun findById(@NotNull @NonNull id: UUID): Optional<GameState?>
}
