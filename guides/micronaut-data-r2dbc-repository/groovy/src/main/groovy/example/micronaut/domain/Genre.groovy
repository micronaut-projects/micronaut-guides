package example.micronaut.domain

import groovy.transform.CompileStatic
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.serde.annotation.Serdeable
import javax.validation.constraints.NotNull

@Serdeable
@CompileStatic
@MappedEntity
class Genre {

    @Id
    @GeneratedValue(GeneratedValue.Type.AUTO)
    Long id

    @NotNull
    String name

    String toString() {
        "Genre{id=$id, name='$name'}"
    }
}

