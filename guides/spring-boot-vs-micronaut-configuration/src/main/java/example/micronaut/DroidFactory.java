package example.micronaut;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Factory;

@Factory
public class DroidFactory {
    @ConfigurationProperties("droid")
    Droid create() {
        return new Droid();
    }
}
