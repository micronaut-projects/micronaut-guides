package example.micronaut;

import io.micronaut.context.ApplicationContext;
import io.micronaut.crac.test.CheckpointSimulator;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HelloControllerTest {
    @Test
    void testHello() throws IOException {
        try(EmbeddedServer server = ApplicationContext.run(EmbeddedServer.class)) {

            HttpClient httpClient = createHttpClient(server);
            BlockingHttpClient client = httpClient.toBlocking();

            String body = client.retrieve("/");
            assertNotNull(body);
            assertEquals("{\"message\":\"Hello World\"}", body);

            CheckpointSimulator checkpointSimulator = server.getApplicationContext().getBean(CheckpointSimulator.class);

            checkpointSimulator.runBeforeCheckpoint();
            assertFalse(server.isRunning());
            client.close();
            httpClient.close();

            checkpointSimulator.runAfterRestore();
            assertTrue(server.isRunning());

            httpClient = createHttpClient(server);
            client = httpClient.toBlocking();
            body = client.retrieve("/");
            assertNotNull(body);
            assertEquals("{\"message\":\"Hello World\"}", body);

            client.close();
            httpClient.close();
        }
    }

    private static HttpClient createHttpClient(EmbeddedServer embeddedServer) {
        String url = "http://localhost:" + embeddedServer.getPort();
        return embeddedServer.getApplicationContext().createBean(HttpClient.class, url);
    }
}