package example.micronaut;

import io.micronaut.context.ApplicationContext;
import io.micronaut.crac.test.CheckpointSimulator;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import java.util.function.Function;

public class CheckpointTestUtils {

    public static void test(Function<BlockingHttpClient, Object> testScenario) {
        try (EmbeddedServer server = ApplicationContext.run(EmbeddedServer.class)) {
            CheckpointSimulator checkpointSimulator =
                    server.getApplicationContext().getBean(CheckpointSimulator.class);
            testApp(server, testScenario);

            checkpointSimulator.runBeforeCheckpoint();
            server.stop();
            checkpointSimulator.runAfterRestore();
            server.start();
            testApp(server, testScenario);
        }
    }

    public static Object testApp(EmbeddedServer embeddedServer, Function<BlockingHttpClient, Object> clientConsumer) {
        try (HttpClient httpClient = embeddedServer.getApplicationContext().createBean(HttpClient.class, embeddedServer.getURL())) {
            BlockingHttpClient client = httpClient.toBlocking();
            return clientConsumer.apply(client);
        }
    }
}
