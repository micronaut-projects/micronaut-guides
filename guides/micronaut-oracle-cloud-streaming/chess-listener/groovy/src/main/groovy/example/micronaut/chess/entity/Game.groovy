package example.micronaut.chess.entity

import example.micronaut.chess.dto.GameDTO
import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Nullable
import io.micronaut.data.annotation.DateCreated
import io.micronaut.data.annotation.DateUpdated
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity

/**
 * Represents a chess game.
 */
@MappedEntity('GAME')
@CompileStatic
class Game {

    @Id
    final String id

    final String blackName

    final String whiteName

    @DateCreated
    Date dateCreated

    @DateUpdated
    Date dateUpdated

    boolean draw

    @Nullable
    String winner

    Game(String id, String blackName, String whiteName) {
        this.id = id
        this.blackName = blackName
        this.whiteName = whiteName
    }

    GameDTO toDto() {
        new GameDTO(id, blackName, whiteName, draw, winner)
    }
}
