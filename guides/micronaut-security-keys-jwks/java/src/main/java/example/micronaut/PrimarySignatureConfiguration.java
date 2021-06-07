package example.micronaut;

import io.micronaut.runtime.context.scope.Refreshable;
import javax.inject.Named;
import javax.inject.Singleton;

@Refreshable // <1>
@Named("generator") // <2>
public class PrimarySignatureConfiguration extends AbstractRSAGeneratorSignatureConfiguration {
    public PrimarySignatureConfiguration(JwkConfiguration jwkConfiguration) {
        super(jwkConfiguration.getPrimary());
    }
}
