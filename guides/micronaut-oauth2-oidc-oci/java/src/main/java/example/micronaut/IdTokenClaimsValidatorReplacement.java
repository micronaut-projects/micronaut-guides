package example.micronaut;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.security.oauth2.client.IdTokenClaimsValidator;
import io.micronaut.security.oauth2.configuration.OauthClientConfiguration;
import io.micronaut.security.oauth2.configuration.OpenIdClientConfiguration;
import jakarta.inject.Singleton;

import java.net.URL;
import java.util.Collection;
import java.util.Optional;

@Replaces(IdTokenClaimsValidator.class)
@Singleton
class IdTokenClaimsValidatorReplacement<T> extends IdTokenClaimsValidator<T> {
    private static final String HTTP = "http://";
    private static final String HTTPS = "https://";
    private static final String EMPTY = "";

    public IdTokenClaimsValidatorReplacement(Collection<OauthClientConfiguration> oauthClientConfigurations) {
        super(oauthClientConfigurations);
    }

    @Override
    protected @NonNull Optional<Boolean> matchesIssuer(@NonNull OpenIdClientConfiguration openIdClientConfiguration, @NonNull String iss) {
        String issWithoutProtocol = removeProtocol(iss);
        return openIdClientConfiguration.getIssuer().map(URL::toString)
                .map(IdTokenClaimsValidatorReplacement::removeProtocol)
                .map(configWithoutProtocol -> issWithoutProtocol.equals(removeProtocol(iss)));
    }

    private static String removeProtocol(String iss) {
        return iss.replace(HTTP, EMPTY)
                .replace(HTTPS, EMPTY);
    }
}