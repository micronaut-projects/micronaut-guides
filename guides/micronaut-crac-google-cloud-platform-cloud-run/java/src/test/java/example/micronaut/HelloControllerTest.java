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

            BlockingHttpClient client = createHttpClient(server);

            String body = client.retrieve("/");
            assertNotNull(body);
            assertEquals("{\"message\":\"Hello World\"}", body);

            CheckpointSimulator checkpointSimulator = server.getApplicationContext().getBean(CheckpointSimulator.class);

            checkpointSimulator.runBeforeCheckpoint();
            assertFalse(server.isRunning());
            client.close();

            checkpointSimulator.runAfterRestore();
            assertTrue(server.isRunning());

            client = createHttpClient(server);
            body = client.retrieve("/");
            assertNotNull(body);
            assertEquals("{\"message\":\"Hello World\"}", body);

            client.close();
        }
    }

    private static BlockingHttpClient createHttpClient(EmbeddedServer embeddedServer) {
        String url = "http://localhost:" + embeddedServer.getPort();
        return embeddedServer.getApplicationContext().createBean(HttpClient.class, url).toBlocking();
    }
}