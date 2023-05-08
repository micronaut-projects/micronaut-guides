package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Serdeable // <1>
@MappedEntity // <2>
public class Pet {

    @Id // <3>
    @GeneratedValue(GeneratedValue.Type.AUTO) // <4>
    private Long id;

    @NonNull
    @NotBlank
    private String name;

    @NonNull
    @NotNull
    private PetType type = PetType.DOG;

    public Pet() {
    }

    public Pet(@NonNull String name) {
        this.name = name;
    }

    public Pet(@NonNull String name, @NonNull PetType type) {
        this.name = name;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public PetType getType() {
        return type;
    }

    public void setType(@NonNull PetType type) {
        this.type = type;
    }
}