package example.micronaut;

import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;

import javax.validation.constraints.NotBlank;

@Introspected
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

    @Creator
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
