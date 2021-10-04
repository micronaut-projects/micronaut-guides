package example.micronaut;

import io.micronaut.runtime.context.scope.Refreshable;

import jakarta.inject.Singleton;

@Refreshable // <1>
public class SecondarySignatureConfiguration extends AbstractRSASignatureConfiguration {
    public SecondarySignatureConfiguration(JwkConfiguration jwkConfiguration) {
        super(jwkConfiguration.getSecondary());
    }
}
