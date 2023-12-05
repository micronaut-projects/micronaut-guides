package example.micronaut;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Property(name = "micronaut.http.client.follow-redirects", value = StringUtils.FALSE) // <1>
@MicronautTest // <2>
class HomeControllerTest {

    @Test
    void homeControllerIsHidden(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        String yml = assertDoesNotThrow(() -> client.retrieve("/swagger/micronaut-guides-1.0.yml"));
        assertFalse(yml.contains("operationId: home")); // <4>
    }
}