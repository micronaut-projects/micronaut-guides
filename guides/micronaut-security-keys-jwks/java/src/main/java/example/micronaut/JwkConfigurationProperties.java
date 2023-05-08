package example.micronaut;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.annotation.NonNull;

import jakarta.validation.constraints.NotBlank;

@ConfigurationProperties("jwk") // <1>
public class JwkConfigurationProperties implements JwkConfiguration {

    @NonNull
    @NotBlank // <2>
    private String primary;

    @NonNull
    @NotBlank // <2>
    private String secondary;

    @Override
    @NonNull
    public String getPrimary() {
        return primary;
    }

    @Override
    @NonNull
    public String getSecondary() {
        return secondary;
    }

    public void setPrimary(@NonNull String primary) {
        this.primary = primary;
    }

    public void setSecondary(@NonNull String secondary) {
        this.secondary = secondary;
    }
}
