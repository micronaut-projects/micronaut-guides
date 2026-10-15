package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.serde.annotation.Serdeable
import org.jspecify.annotations.Nullable

@CompileStatic
@Serdeable // <1>
@MappedEntity // <2>
class Book {

    @Id // <3>
    Long id

    String title

    @Nullable
    Integer pages
}
