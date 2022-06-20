package example.micronaut;

import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.context.scope.Refreshable;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static io.micronaut.core.annotation.AnnotationUtil.NAMED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
public class PrimarySignatureConfigurationTest {

    @Inject
    ApplicationContext applicationContext;

    @Test
    void primarySignatureConfigurationIsAnnotatedWithRefreshable() {
        assertTrue(applicationContext.getBeanDefinition(PrimarySignatureConfiguration.class)
                .getAnnotationNameByStereotype(Refreshable.class)
                .isPresent());
    }

    @Test
    void primarySignatureConfigurationIsAnnotatedNamedGenerator() {
        assertNotNull(applicationContext.getBeanDefinition(PrimarySignatureConfiguration.class).getAnnotationMetadata()
                .getAnnotation(NAMED));

        assertTrue(applicationContext.getBeanDefinition(PrimarySignatureConfiguration.class).getAnnotationMetadata()
                .getAnnotation(NAMED)
                .getValue(String.class).isPresent());

        assertEquals("generator", applicationContext.getBeanDefinition(PrimarySignatureConfiguration.class).getAnnotationMetadata()
                .getAnnotation(NAMED)
                .getValue(String.class).get());
    }
}
