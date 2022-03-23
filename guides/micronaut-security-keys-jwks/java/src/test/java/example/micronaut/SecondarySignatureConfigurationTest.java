package example.micronaut;

import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.context.scope.Refreshable;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
public class SecondarySignatureConfigurationTest {

    @Inject
    ApplicationContext applicationContext;

    @Test
    void secondarySignatureConfigurationIsAnnotatedWithRefreshable() {
        assertTrue(applicationContext.getBeanDefinition(SecondarySignatureConfiguration.class)
                .getAnnotationNameByStereotype(Refreshable.class)
                .isPresent());
    }
}
