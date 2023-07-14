package example.micronaut.chess.entity

import example.micronaut.chess.dto.Player
import example.micronaut.chess.dto.GameDTO
import groovy.transform.CompileStatic
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import jakarta.validation.constraints.NotBlank
import io.micronaut.data.annotation.DateCreated
import io.micronaut.data.annotation.DateUpdated
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

@MappedEntity('GAME')
@CompileStatic
class Game {

    @Id
    @NotNull
    final UUID id

    @Size(max = 255)
    @NotBlank
    @NonNull
    final String blackName

    @Size(max = 255)
    @NotBlank
    @NonNull
    final String whiteName

    @DateCreated
    LocalDateTime dateCreated

    @DateUpdated
    LocalDateTime dateUpdated

    boolean draw

    @Nullable
    @Size(max = 1)
    Player winner

    Game(@NonNull UUID id,
         @NonNull String blackName,
         @NonNull String whiteName) {
        this.id = id
        this.blackName = blackName
        this.whiteName = whiteName
    }

    GameDTO toDto() {
        new GameDTO(id.toString(), blackName, whiteName, draw, winner)
    }
}
