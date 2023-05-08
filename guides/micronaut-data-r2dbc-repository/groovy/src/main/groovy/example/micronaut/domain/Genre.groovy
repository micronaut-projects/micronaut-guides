package example.micronaut.domain

import groovy.transform.CompileStatic
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.NotBlank

@Serdeable
@CompileStatic
@MappedEntity
class Genre {

    @Id
    @GeneratedValue(GeneratedValue.Type.AUTO)
    Long id

    @NotBlank
    String name

    String toString() {
        "Genre{id=$id, name='$name'}"
    }
}

