package example.micronaut;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;

@Requires(property = "app.hosted-domain")
@ConfigurationProperties("app")
public class ApplicationConfigurationProperties implements ApplicationConfiguration {

    @NonNull
    private String hostedDomain;

    public void setHostedDomain(@NonNull String hostedDomain) {
        this.hostedDomain = hostedDomain;
    }

    @Override
    @NonNull
    public String getHostedDomain() {
        return hostedDomain;
    }
}
