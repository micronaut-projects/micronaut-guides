package example.micronaut

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.GeneratedValue.Type.AUTO
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import jakarta.validation.constraints.NotNull

@MappedEntity // <1>
class Author(val username: @NotNull String?) {

    @Id // <2>
    @GeneratedValue(AUTO)
    var id: Long? = null
}