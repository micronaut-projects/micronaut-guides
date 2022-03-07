package example.micronaut;

import io.micronaut.runtime.context.scope.Refreshable;
import jakarta.inject.Named;

@Refreshable // <1>
@Named("generator") // <2>
public class PrimarySignatureConfiguration extends AbstractRSAGeneratorSignatureConfiguration {

    public PrimarySignatureConfiguration(JwkConfiguration jwkConfiguration) {
        super(jwkConfiguration.getPrimary());
    }
}
