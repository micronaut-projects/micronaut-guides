package example.micronaut;

import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.context.scope.Refreshable;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@MicronautTest(startApplication = false)
public class SecondarySignatureConfigurationTest {
    @Inject
    ApplicationContext applicationContext;

    @Test
    void secondarySignatureConfigurationIsAnnotatedWithRefreshable() {
        Assertions.assertTrue(applicationContext.getBeanDefinition(SecondarySignatureConfiguration.class)
                .getAnnotationNameByStereotype(Refreshable.class)
                .isPresent());
    }
}
