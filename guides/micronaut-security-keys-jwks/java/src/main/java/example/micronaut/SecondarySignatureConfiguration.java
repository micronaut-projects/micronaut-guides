package example.micronaut;

import io.micronaut.runtime.context.scope.Refreshable;
import jakarta.inject.Named;

@Named("secondary")
@Refreshable // <1>
public class SecondarySignatureConfiguration extends AbstractRSASignatureConfiguration {

    public SecondarySignatureConfiguration(JwkConfiguration jwkConfiguration) {
        super(jwkConfiguration.getSecondary());
    }
}
