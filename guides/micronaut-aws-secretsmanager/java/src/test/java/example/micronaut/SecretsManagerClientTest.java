package example.micronaut;

import io.micronaut.context.ApplicationContext;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import jakarta.inject.Inject;

@MicronautTest(startApplication = false)
public class SecretsManagerClientTest {
    @Inject
    ApplicationContext applicationContext;

    @Test
    void testBeanOfTypeSecretsManagerClientExists() {
        assertTrue(applicationContext.containsBean(SecretsManagerClient.class));
    }
}
