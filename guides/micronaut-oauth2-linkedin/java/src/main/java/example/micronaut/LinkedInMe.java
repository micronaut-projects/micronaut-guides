package example.micronaut;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;

import javax.validation.constraints.NotBlank;

@Introspected
public class LinkedInMe {

    @NonNull
    @NotBlank
    private String id;

    @NonNull
    @NotBlank
    private String localizedFirstName;

    @NonNull
    @NotBlank
    private String localizedLastName;

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

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getLocalizedFirstName() {
        return localizedFirstName;
    }

    public void setLocalizedFirstName(@NonNull String localizedFirstName) {
        this.localizedFirstName = localizedFirstName;
    }

    @NonNull
    public String getLocalizedLastName() {
        return localizedLastName;
    }

    public void setLocalizedLastName(@NonNull String localizedLastName) {
        this.localizedLastName = localizedLastName;
    }
}
