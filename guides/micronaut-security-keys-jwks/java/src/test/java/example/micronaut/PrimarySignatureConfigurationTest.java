package example.micronaut;

import static org.junit.jupiter.api.Assertions.assertTrue;
import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.context.scope.Refreshable;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        String annotationName = "javax.inject.Named";
        assertNotNull(applicationContext.getBeanDefinition(PrimarySignatureConfiguration.class).getAnnotationMetadata()
                .getAnnotation(annotationName));
        assertTrue(applicationContext.getBeanDefinition(PrimarySignatureConfiguration.class).getAnnotationMetadata()
                .getAnnotation(annotationName)
                .getValue(String.class).isPresent());
        assertEquals("generator", applicationContext.getBeanDefinition(PrimarySignatureConfiguration.class).getAnnotationMetadata()
                .getAnnotation(annotationName)
                .getValue(String.class).get());
    }
}
