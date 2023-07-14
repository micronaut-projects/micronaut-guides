package example.micronaut;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;

import jakarta.validation.constraints.NotBlank;

@Introspected
public class Contact {

    @E164
    @NotBlank
    @NonNull
    private final String phone;

    public Contact(@NonNull String phone) {
        this.phone = phone;
    }


    @NonNull
    public String getPhone() {
        return phone;
    }
}
