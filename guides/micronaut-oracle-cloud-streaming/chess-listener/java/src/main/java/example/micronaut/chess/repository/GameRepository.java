package example.micronaut.chess.repository;

import example.micronaut.chess.entity.Game;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.UUID;

/**
 * <code>Game</code> entity repository.
 */
@Repository
public interface GameRepository extends CrudRepository<Game, UUID> {
}
