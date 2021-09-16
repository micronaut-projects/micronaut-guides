package example.micronaut;

import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import javax.validation.constraints.NotBlank;

@Introspected // <1>
public class Fruit {

    @NonNull
    @NotBlank // <2>
    private String name;

    @Nullable
    private String description;

    public Fruit() {

    }
    
    public Fruit(@NonNull String name) {
        this(name, null);        
    }
    
    @Creator
    public Fruit(@NonNull String name, @Nullable String description) {
        this.name = name;
        this.description = description;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setDescription(@Nullable String description) {
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
