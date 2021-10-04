package example.micronaut;

import io.micronaut.context.ApplicationContext;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

@MicronautTest(startApplication = false)
public class JwkConfigurationTest {

    @Inject
    ApplicationContext applicationContext;

    @Test
    void beanOfTypeJwkConfigurationExists() {
        applicationContext.containsBean(JwkConfiguration.class);
    }
}
