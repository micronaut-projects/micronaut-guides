package example.micronaut;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.type.Argument;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
@Property(name = "endpoints.info.enabled", value = StringUtils.TRUE)
@Property(name = "endpoints.info.sensitive", value = StringUtils.FALSE)
@MicronautTest
public class InfoEndpointTest {

    @Test
    void cracInformationIsExposedInTheInfoEndpointExposed(@Client("/") HttpClient client) {
        Map<String, Map<String, Integer>> json = assertDoesNotThrow(() -> client.toBlocking().retrieve(HttpRequest.GET("/info"), Argument.mapOf(Argument.of(String.class), Argument.mapOf(String.class, Integer.class))));
        assertNotNull(json);
        assertEquals(Map.of("crac", Map.of("restore-time", -1, "uptime-since-restore", -1)), json);
    }
}