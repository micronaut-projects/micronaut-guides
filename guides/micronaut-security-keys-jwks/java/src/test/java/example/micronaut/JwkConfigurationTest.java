package example.micronaut;

import io.micronaut.context.ApplicationContext;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
public class JwkConfigurationTest {

    @Inject
    ApplicationContext applicationContext;

    @Test
    void beanOfTypeJwkConfigurationExists() {
        assertTrue(applicationContext.containsBean(JwkConfiguration.class));
    }
}
