package example.micronaut;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.security.oauth2.client.IdTokenClaimsValidator;
import io.micronaut.security.oauth2.configuration.OauthClientConfiguration;
import io.micronaut.security.oauth2.configuration.OpenIdClientConfiguration;
import jakarta.inject.Singleton;

import java.util.Collection;
import java.util.Optional;

@Replaces(IdTokenClaimsValidator.class)
@Singleton
class IdTokenClaimsValidatorReplacement<T> extends IdTokenClaimsValidator<T> {
    public IdTokenClaimsValidatorReplacement(Collection<OauthClientConfiguration> oauthClientConfigurations) {
        super(oauthClientConfigurations);
    }

    @Override
    protected @NonNull Optional<Boolean> matchesIssuer(@NonNull OpenIdClientConfiguration openIdClientConfiguration, @NonNull String iss) {
        return Optional.of(Boolean.TRUE);
    }
}
