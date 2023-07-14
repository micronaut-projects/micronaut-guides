package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;

@Serdeable
public class LinkedInMe {

    @NonNull
    @NotBlank
    private final String id;

    @NonNull
    @NotBlank
    private final String localizedFirstName;

    @NonNull
    @NotBlank
    private final String localizedLastName;

    public LinkedInMe(@NonNull String id,
                      @NonNull String localizedFirstName,
                      @NonNull String localizedLastName) {
        this.id = id;
        this.localizedFirstName = localizedFirstName;
        this.localizedLastName = localizedLastName;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getLocalizedFirstName() {
        return localizedFirstName;
    }

    @NonNull
    public String getLocalizedLastName() {
        return localizedLastName;
    }
}
