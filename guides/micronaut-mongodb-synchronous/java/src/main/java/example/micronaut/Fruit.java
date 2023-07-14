package example.micronaut;

import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import jakarta.validation.constraints.NotBlank;

@Serdeable // <1>
public class Fruit {

    @NonNull
    @NotBlank // <2>
    @BsonProperty("name") // <3>
    private final String name;

    @Nullable
    @BsonProperty("description") // <3>
    private final String description;

    public Fruit(@NonNull String name) {
        this(name, null);
    }

    @Creator // <4>
    @BsonCreator// <3>
    public Fruit(@NonNull @BsonProperty("name") String name,  // <3>
                 @Nullable @BsonProperty("description") String description) {  // <3>
        this.name = name;
        this.description = description;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @Nullable
    public String getDescription() {
        return description;
    }
}
