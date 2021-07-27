package example.micronaut.chess.repository;

import example.micronaut.chess.entity.GameState;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.Join;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

import static io.micronaut.data.annotation.Join.Type.FETCH;

/**
 * <code>GameState</code> entity repository.
 */
@Repository
public interface GameStateRepository extends CrudRepository<GameState, UUID> {

    @Override
    @NonNull
    @Join(value = "game", type = FETCH) // <1>
    Optional<GameState> findById(@NotNull @NonNull UUID id);
}
