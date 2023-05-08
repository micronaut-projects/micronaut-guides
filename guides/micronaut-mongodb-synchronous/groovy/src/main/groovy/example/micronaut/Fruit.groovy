package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Creator
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import io.micronaut.serde.annotation.Serdeable
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonProperty

import jakarta.validation.constraints.NotBlank

@CompileStatic
@Serdeable // <1>
class Fruit {

    @NonNull
    @NotBlank // <2>
    @BsonProperty('name') // <3>
    private final String name

    @Nullable
    @BsonProperty('description') // <3>
    private final String description

    Fruit(@NonNull String name) {
        this(name, null)
    }

    @Creator // <4>
    @BsonCreator// <3>
    Fruit(@NonNull @BsonProperty('name') String name,  // <3>
          @Nullable @BsonProperty('description') String description) {  // <3>
        this.name = name
        this.description = description
    }

    @NonNull
    String getName() {
        name
    }

    @Nullable
    String getDescription() {
        description
    }
}
