package example.micronaut;

import io.micronaut.context.ApplicationContext;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.http.SdkHttpClient;
import javax.inject.Inject;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
public class SdkHttpClientTest {
    @Inject
    ApplicationContext applicationContext;

    @Test
    void testBeanOfTypeSdkHttpClientExists() {
        assertTrue(applicationContext.containsBean(SdkHttpClient.class));
    }
}
