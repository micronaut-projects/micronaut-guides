package example.micronaut;

import io.micronaut.context.BeanContext;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest // <1>
class HelloControllerTest {
    @Inject
    BeanContext beanContext;

    @Test
    void testHello() {
        HttpClient client = createHttpClient(beanContext);
        String body = client.toBlocking().retrieve(HttpRequest.GET("/"));
        assertNotNull(body);
        assertEquals("{\"message\":\"Hello World\"}", body);
    }

    private static HttpClient createHttpClient(BeanContext beanContext) {
        return beanContext.createBean(HttpClient.class, "http://localhost:" + beanContext.getBean(EmbeddedServer.class).getPort());
    }
}